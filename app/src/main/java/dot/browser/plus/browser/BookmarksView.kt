package dot.browser.plus.browser

import dot.browser.plus.database.HistoryItem

interface BookmarksView {

    fun navigateBack()

    fun handleUpdatedUrl(url: String)

    fun handleBookmarkDeleted(item: HistoryItem)

}
