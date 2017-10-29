package dot.browser.plus.search.suggestions

import dot.browser.plus.database.HistoryItem
import dot.browser.plus.extensions.safeUse
import dot.browser.plus.utils.FileUtils
import android.app.Application
import android.text.TextUtils
import android.util.Log
import io.reactivex.Single
import okhttp3.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.net.URL
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * The base search suggestions API. Provides common fetching and caching functionality for each
 * potential suggestions provider.
 */
abstract class BaseSuggestionsModel internal constructor(
        application: Application, private val encoding: String
) : SuggestionsRepository {

    private val httpClient: OkHttpClient
    private val cacheControl = CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build()

    /**
     * Create a URL for the given query in the given language.
     *
     * @param query    the query that was made.
     * @param language the locale of the user.
     * @return should return a URL that can be fetched using a GET.
     */
    protected abstract fun createQueryUrl(query: String, language: String): String

    /**
     * Parse the results of an input stream into a list of [HistoryItem].
     *
     * @param inputStream the raw input to parse.
     */
    @Throws(Exception::class)
    protected abstract fun parseResults(inputStream: InputStream): List<HistoryItem>

    init {
        val suggestionsCache = File(application.cacheDir, "suggestion_responses")
        httpClient = OkHttpClient.Builder()
                .cache(Cache(suggestionsCache, FileUtils.megabytesToBytes(1)))
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .build()
    }

    override fun resultsForSearch(rawQuery: String): Single<List<HistoryItem>> = Single.fromCallable {
        val filter = ArrayList<HistoryItem>(5)

        val query = try {
            URLEncoder.encode(rawQuery, encoding)
        } catch (e: UnsupportedEncodingException) {
            Log.e(TAG, "Unable to encode the URL", e)

            return@fromCallable filter
        }

        downloadSuggestionsForQuery(query, language)?.let(Response::body)?.safeUse {
            filter += parseResults(it.byteStream()).take(MAX_RESULTS)
        }

        return@fromCallable filter
    }

    /**
     * This method downloads the search suggestions for the specific query.
     * NOTE: This is a blocking operation, do not fetchResults on the UI thread.
     *
     * @param query the query to get suggestions for
     *
     * @return the cache file containing the suggestions
     */
    private fun downloadSuggestionsForQuery(query: String, language: String): Response? {
        val queryUrl = createQueryUrl(query, language)

        try {
            val url = URL(queryUrl)

            // OkHttp automatically gzips requests
            val suggestionsRequest = Request.Builder().url(url)
                    .addHeader("Accept-Charset", encoding)
                    .cacheControl(cacheControl)
                    .build()

            return httpClient.newCall(suggestionsRequest).execute()
        } catch (exception: IOException) {
            Log.e(TAG, "Problem getting search suggestions", exception)
        }

        return null
    }

    companion object {

        private const val TAG = "BaseSuggestionsModel"

        private const val MAX_RESULTS = 5
        private val INTERVAL_DAY = TimeUnit.DAYS.toSeconds(1)
        private const val DEFAULT_LANGUAGE = "en"

        private val language by lazy {
            var lang = Locale.getDefault().language
            if (TextUtils.isEmpty(lang)) {
                lang = DEFAULT_LANGUAGE
            }

            lang
        }

        private val REWRITE_CACHE_CONTROL_INTERCEPTOR = Interceptor { chain ->
            val originalResponse = chain.proceed(chain.request())
            originalResponse.newBuilder()
                    .header("cache-control", "max-age=$INTERVAL_DAY, max-stale=$INTERVAL_DAY")
                    .build()
        }
    }

}
