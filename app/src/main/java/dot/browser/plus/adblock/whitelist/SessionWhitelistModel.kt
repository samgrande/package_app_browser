package dot.browser.plus.adblock.whitelist

import dot.browser.plus.database.whitelist.AdBlockWhitelistRepository
import dot.browser.plus.database.whitelist.WhitelistItem
import dot.browser.plus.utils.domainForUrl
import android.util.Log
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * An in memory representation of the ad blocking whitelist. Can be queried synchronously.
 */
@Singleton
class SessionWhitelistModel @Inject constructor(
        private val adBlockWhitelistModel: AdBlockWhitelistRepository,
        @Named("database") private val ioScheduler: Scheduler
) : WhitelistModel {

    private var whitelistSet = hashSetOf<String>()

    init {
        adBlockWhitelistModel
                .allWhitelistItems()
                .subscribeOn(ioScheduler)
                .subscribe { list ->
                    whitelistSet = list.map(WhitelistItem::url).let { it }.toHashSet()
                }
    }

    override fun isUrlWhitelisted(url: String): Boolean = whitelistSet.contains(domainForUrl(url))

    override fun addUrlToWhitelist(url: String) {
        domainForUrl(url)?.let { domain ->
            val whitelistItem = WhitelistItem(domain, System.currentTimeMillis())
            adBlockWhitelistModel
                    .whitelistItemForUrl(domain)
                    .isEmpty
                    .filter { it }
                    .flatMapCompletable { adBlockWhitelistModel.addWhitelistItem(whitelistItem) }
                    .subscribeOn(ioScheduler)
                    .subscribe {
                        Log.d(TAG, "whitelist item added to database")
                    }

            whitelistSet.add(domain)
        }
    }

    override fun removeUrlFromWhitelist(url: String) {
        domainForUrl(url)?.let { domain ->
            adBlockWhitelistModel.whitelistItemForUrl(domain)
                    .flatMapCompletable(adBlockWhitelistModel::removeWhitelistItem)
                    .subscribeOn(ioScheduler)
                    .subscribe { Log.d(TAG, "whitelist item removed from database") }

            whitelistSet.remove(domain)
        }
    }

    companion object {
        private const val TAG = "SessionWhitelistModel"
    }
}