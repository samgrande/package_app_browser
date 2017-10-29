package dot.browser.plus.search.engine

import dot.browser.plus.R

/**
 * A custom search engine.
 */
class CustomSearch(queryUrl: String) : BaseSearchEngine(
        "file:///android_asset/lightning.png",
        queryUrl,
        R.string.search_engine_custom
)
