package dot.browser.plus.di

import dot.browser.plus.adblock.whitelist.SessionWhitelistModel
import dot.browser.plus.adblock.whitelist.WhitelistModel
import dot.browser.plus.database.bookmark.BookmarkDatabase
import dot.browser.plus.database.bookmark.BookmarkRepository
import dot.browser.plus.database.downloads.DownloadsDatabase
import dot.browser.plus.database.downloads.DownloadsRepository
import dot.browser.plus.database.history.HistoryDatabase
import dot.browser.plus.database.history.HistoryRepository
import dot.browser.plus.database.whitelist.AdBlockWhitelistDatabase
import dot.browser.plus.database.whitelist.AdBlockWhitelistRepository
import dot.browser.plus.ssl.SessionSslWarningPreferences
import dot.browser.plus.ssl.SslWarningPreferences
import dagger.Binds
import dagger.Module

/**
 * Dependency injection module used to bind implementations to interfaces.
 */
@Module
abstract class LightningModule {

    @Binds
    abstract fun provideBookmarkModel(bookmarkDatabase: BookmarkDatabase): BookmarkRepository

    @Binds
    abstract fun provideDownloadsModel(downloadsDatabase: DownloadsDatabase): DownloadsRepository

    @Binds
    abstract fun providesHistoryModel(historyDatabase: HistoryDatabase): HistoryRepository

    @Binds
    abstract fun providesAdBlockWhitelistModel(adBlockWhitelistDatabase: AdBlockWhitelistDatabase): AdBlockWhitelistRepository

    @Binds
    abstract fun providesWhitelistModel(sessionWhitelistModel: SessionWhitelistModel): WhitelistModel

    @Binds
    abstract fun providesSslWarningPreferences(sessionSslWarningPreferences: SessionSslWarningPreferences): SslWarningPreferences

}