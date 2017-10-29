package dot.browser.plus.di

import dot.browser.plus.BrowserApp
import dot.browser.plus.adblock.AssetsAdBlocker
import dot.browser.plus.adblock.NoOpAdBlocker
import dot.browser.plus.browser.BrowserPresenter
import dot.browser.plus.browser.SearchBoxModel
import dot.browser.plus.browser.TabsManager
import dot.browser.plus.browser.activity.BrowserActivity
import dot.browser.plus.browser.activity.ThemableBrowserActivity
import dot.browser.plus.browser.fragment.BookmarksFragment
import dot.browser.plus.browser.fragment.TabsFragment
import dot.browser.plus.dialog.LightningDialogBuilder
import dot.browser.plus.download.DownloadHandler
import dot.browser.plus.download.LightningDownloadListener
import dot.browser.plus.html.bookmark.BookmarkPage
import dot.browser.plus.html.download.DownloadsPage
import dot.browser.plus.html.history.HistoryPage
import dot.browser.plus.html.homepage.StartPage
import dot.browser.plus.network.NetworkConnectivityModel
import dot.browser.plus.reading.activity.ReadingActivity
import dot.browser.plus.search.SearchEngineProvider
import dot.browser.plus.search.SuggestionsAdapter
import dot.browser.plus.settings.activity.ThemableSettingsActivity
import dot.browser.plus.settings.fragment.*
import dot.browser.plus.utils.ProxyUtils
import dot.browser.plus.view.LightningChromeClient
import dot.browser.plus.view.LightningView
import dot.browser.plus.view.LightningWebClient
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, LightningModule::class))
interface AppComponent {

    fun inject(activity: BrowserActivity)

    fun inject(fragment: BookmarksFragment)

    fun inject(fragment: BookmarkSettingsFragment)

    fun inject(builder: LightningDialogBuilder)

    fun inject(fragment: TabsFragment)

    fun inject(lightningView: LightningView)

    fun inject(activity: ThemableBrowserActivity)

    fun inject(fragment: LightningPreferenceFragment)

    fun inject(app: BrowserApp)

    fun inject(proxyUtils: ProxyUtils)

    fun inject(activity: ReadingActivity)

    fun inject(webClient: LightningWebClient)

    fun inject(activity: ThemableSettingsActivity)

    fun inject(listener: LightningDownloadListener)

    fun inject(fragment: PrivacySettingsFragment)

    fun inject(startPage: StartPage)

    fun inject(historyPage: HistoryPage)

    fun inject(bookmarkPage: BookmarkPage)

    fun inject(downloadsPage: DownloadsPage)

    fun inject(presenter: BrowserPresenter)

    fun inject(manager: TabsManager)

    fun inject(fragment: DebugSettingsFragment)

    fun inject(suggestionsAdapter: SuggestionsAdapter)

    fun inject(chromeClient: LightningChromeClient)

    fun inject(downloadHandler: DownloadHandler)

    fun inject(searchBoxModel: SearchBoxModel)

    fun inject(searchEngineProvider: SearchEngineProvider)

    fun inject(generalSettingsFragment: GeneralSettingsFragment)

    fun inject(networkConnectivityModel: NetworkConnectivityModel)

    fun provideAssetsAdBlocker(): AssetsAdBlocker

    fun provideNoOpAdBlocker(): NoOpAdBlocker

}
