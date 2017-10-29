package dot.browser.plus.search.engine

import dot.browser.plus.R

/**
 * The StartPage mobile search engine.
 */
class StartPageMobileSearch : BaseSearchEngine(
        "file:///android_asset/startpage.png",
        "https://startpage.com/do/m/mobilesearch?language=english&query=",
        R.string.search_engine_startpage_mobile
)
