package dot.browser.plus.html.history

import com.anthonycr.mezzanine.FileStream

/**
 * The store for the list view HTML.
 */
@FileStream("app/src/main/html/list.html")
interface ListPageReader {

    fun provideHtml(): String

}