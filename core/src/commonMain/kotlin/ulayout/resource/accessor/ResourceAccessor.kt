package ulayout.resource.accessor

import okio.Path
import org.w3c.dom.HTMLImageElement
import org.w3c.xhr.XMLHttpRequest
import ulayout.util.normalizeToString
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ResourceAccessor {
    companion object Default : ResourceAccessor {
        private fun get(path: Path, config: (XMLHttpRequest.() -> Unit) = {}) = XMLHttpRequest().apply {
            open("GET", path.normalizeToString(), false)
            println("GET ${path.normalizeToString()}")
            config()
            send()
        }

        private fun XMLHttpRequest.exists() = status in 200..299

        override fun exists(path: Path) = get(path).exists()

        override fun readTextOrNull(path: Path): String? =
            get(path).takeIf { it.exists() }?.responseText

        override fun readBytesOrNull(path: Path): ByteArray? =
            get(path) {
                overrideMimeType("text/plain; charset=x-user-defined")
            }.takeIf { it.exists() }
                ?.let { response ->
                    val raw = response.responseText
                    ByteArray(raw.length) { raw[it].code.toUByte().toByte() }
                }
    }

    fun exists(path: Path): Boolean
    fun readTextOrNull(path: Path): String?
    fun readBytesOrNull(path: Path): ByteArray?
    fun readText(path: Path): String = readTextOrNull(path) ?: error("Cannot found $path")
    fun readBytes(path: Path): ByteArray = readBytesOrNull(path) ?: error("Cannot found $path")
    fun requireExists(path: Path?): Path {
        require(path != null && exists(path)) { "Cannot found $path)" }
        return path
    }
    suspend fun <T> attach(path: Path, attach: suspend (String) -> T): T {
        requireExists(path)
        return attach(path.normalizeToString())
    }
}

suspend inline fun <T : HTMLImageElement> ResourceAccessor.attachTo(path: Path, image: T): T =
    attach(path) { src ->
        image.src = src
        suspendCoroutine { continuation ->
            image.onload = {
                continuation.resume(image)
            }
            image.onerror = { _, cause, _, _, _ ->
                continuation.resumeWithException(Exception(cause))
            }
        }
    }

