package dot.browser.plus.search.suggestions

import dot.browser.plus.database.HistoryItem
import io.reactivex.Single

/**
 * A search suggestions repository that doesn't fetch any results.
 */
class NoOpSuggestionsRepository : SuggestionsRepository {

    private val emptySingle: Single<List<HistoryItem>> = Single.just(listOf())

    override fun resultsForSearch(rawQuery: String) = emptySingle
}