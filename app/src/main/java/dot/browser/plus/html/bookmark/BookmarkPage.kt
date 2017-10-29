/*
 * Copyright 2014 A.C.R. Development
 */
package dot.browser.plus.html.bookmark

import dot.browser.plus.BrowserApp
import dot.browser.plus.R
import dot.browser.plus.constant.FILE
import dot.browser.plus.database.HistoryItem
import dot.browser.plus.database.bookmark.BookmarkRepository
import dot.browser.plus.extensions.safeUse
import dot.browser.plus.favicon.FaviconModel
import dot.browser.plus.utils.ThemeUtils
import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.text.TextUtils
import com.anthonycr.bonsai.Single
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import javax.inject.Inject
import javax.inject.Named

class BookmarkPage(activity: Activity) {

    @Inject internal lateinit var app: Application
    @Inject internal lateinit var bookmarkModel: BookmarkRepository
    @Inject internal lateinit var faviconModel: FaviconModel
    @Inject @field:Named("database") internal lateinit var databaseScheduler: Scheduler

    private val folderIcon = ThemeUtils.getThemedBitmap(activity, R.drawable.ic_folder, false)

    init {
        BrowserApp.appComponent.inject(this)
    }

    fun createBookmarkPage(): Single<String> = Single.create { subscriber ->
        cacheIcon(folderIcon, getFaviconFile(app))
        cacheIcon(faviconModel.getDefaultBitmapForString(null), getDefaultIconFile(app))
        buildBookmarkPage(null)

        val bookmarkWebPage = getBookmarkPage(app, null)

        subscriber.onItem("$FILE$bookmarkWebPage")
        subscriber.onComplete()
    }

    private fun cacheIcon(icon: Bitmap, file: File) = FileOutputStream(file).safeUse {
        icon.compress(Bitmap.CompressFormat.PNG, 100, it)
        icon.recycle()
    }

    private fun buildBookmarkPage(folder: String?) {
        bookmarkModel.getBookmarksFromFolderSorted(folder)
                .concatWith(io.reactivex.Single.defer {
                    if (folder == null) {
                        bookmarkModel.getFoldersSorted()
                    } else {
                        io.reactivex.Single.just(listOf())
                    }
                }).toList()
                .map { it.flatMap { it }.toMutableList() }
                .subscribeOn(databaseScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bookmarksAndFolders ->
                    bookmarksAndFolders.sort()
                    buildPageHtml(bookmarksAndFolders, folder)
                }
    }

    private fun buildPageHtml(bookmarksAndFolders: List<HistoryItem>, folder: String?) {
        val bookmarkWebPage = getBookmarkPage(app, folder)

        val builder = BookmarkPageBuilder(faviconModel, app)

        FileWriter(bookmarkWebPage, false).use {
            it.write(builder.buildPage(bookmarksAndFolders))
        }

        bookmarksAndFolders
                .filter { it.isFolder }
                .forEach { buildBookmarkPage(it.title) }
    }

    companion object {

        /**
         * The bookmark page standard suffix
         */
        const val FILENAME = "bookmarks.html"

        private const val FOLDER_ICON = "folder.png"
        private const val DEFAULT_ICON = "default.png"

        @JvmStatic
        fun getBookmarkPage(application: Application, folder: String?): File {
            val prefix = if (!TextUtils.isEmpty(folder)) folder + '-' else ""
            return File(application.filesDir, prefix + FILENAME)
        }

        private fun getFaviconFile(application: Application): File =
                File(application.cacheDir, FOLDER_ICON)

        private fun getDefaultIconFile(application: Application): File =
                File(application.cacheDir, DEFAULT_ICON)
    }

}
