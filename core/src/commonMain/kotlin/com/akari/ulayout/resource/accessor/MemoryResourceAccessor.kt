@file:OptIn(ExperimentalEncodingApi::class)

package com.akari.ulayout.resource.accessor

import okio.Path
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/*
 * Storage Strategy:
 * binaries to base64
 * image to data:src
 */
typealias ResourceStorage = Map<Path, String>

class MemoryResourceAccessor(
    private val storage: ResourceStorage
) : ResourceAccessor {
    override fun exists(path: Path) = storage.containsKey(path)
    override fun readTextOrNull(path: Path) = storage[path]
    override fun readBytesOrNull(path: Path): ByteArray? = readTextOrNull(path)?.let(Base64::decode)
    override suspend fun <T> attach(path: Path, attach: suspend (String) -> T): T {
        requireExists(path)
        return attach(readText(path))
    }
    override fun toString() = "MemoryResourceAccessor"
}