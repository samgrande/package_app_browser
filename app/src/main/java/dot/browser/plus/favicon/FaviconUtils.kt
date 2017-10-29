@file:JvmName("FaviconUtils")

package dot.browser.plus.favicon

import android.net.Uri

/**
 * Requires that a [Uri] be safe for use. The [Uri.getScheme] and [Uri.getHost] must not be
 * `null` or blank.
 */
fun requireUriSafe(uri: Uri) {
    if (uri.scheme?.isNotBlank() != true || uri.host?.isNotBlank() != true) {
        throw RuntimeException("Unsafe uri provided")
    }
}
