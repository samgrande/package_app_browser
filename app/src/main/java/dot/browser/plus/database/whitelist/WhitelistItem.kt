package dot.browser.plus.database.whitelist

/**
 * A model object representing a whitelisted URL.
 */
data class WhitelistItem(
        val url: String,
        val timeCreated: Long
)