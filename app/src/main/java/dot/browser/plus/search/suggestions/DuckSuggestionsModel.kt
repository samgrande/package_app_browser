package dot.browser.plus.search.suggestions

import dot.browser.plus.R
import dot.browser.plus.constant.UTF8
import dot.browser.plus.database.HistoryItem
import dot.browser.plus.extensions.map
import dot.browser.plus.utils.FileUtils
import android.app.Application
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

/**
 * The search suggestions provider for the DuckDuckGo search engine.
 */
class DuckSuggestionsModel(application: Application) : BaseSuggestionsModel(application, UTF8) {

    private val searchSubtitle = application.getString(R.string.suggestion)

    override fun createQueryUrl(query: String, language: String): String =
            "https://duckduckgo.com/ac/?q=$query"

    @Throws(Exception::class)
    override fun parseResults(inputStream: InputStream): List<HistoryItem> {
        val content = FileUtils.readStringFromStream(inputStream, UTF8)
        val jsonArray = JSONArray(content)

        return jsonArray
                .map { it as JSONObject }
                .map { it.getString("phrase") }
                .map { HistoryItem("$searchSubtitle \"$it\"", it, R.drawable.ic_search) }
    }

}
