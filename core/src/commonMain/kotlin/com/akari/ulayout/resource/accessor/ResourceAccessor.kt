package com.akari.ulayout.resource.accessor

import com.akari.ulayout.util.normalizeToString
import com.akari.ulayout.util.toHttpUrl
import okio.Path
import org.w3c.dom.HTMLImageElement
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ResourceAccessor {
    companion object Default : ResourceAccessor {
        override suspend fun exists(path: Path) =
            path.normalizeToString().toHttpUrl().exists()

        override suspend fun readTextOrNull(path: Path): String? =
            path.normalizeToString().toHttpUrl().readTextOrNull()

        override suspend fun readBytesOrNull(path: Path): ByteArray? =
            path.normalizeToString().toHttpUrl().readBytesOrNull()
    }

    suspend fun exists(path: Path): Boolean
    suspend fun readTextOrNull(path: Path): String?
    suspend fun readBytesOrNull(path: Path): ByteArray?
    suspend fun readText(path: Path): String = readTextOrNull(path) ?: error("Cannot found $path")
    suspend fun readBytes(path: Path): ByteArray = readBytesOrNull(path) ?: error("Cannot found $path")
    suspend fun requireExists(path: Path?): Path {
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

