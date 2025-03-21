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

fun ResourceStorage.asMemoryRA() = MemoryResourceAccessor(this)

class MemoryResourceAccessor(
    private val storage: ResourceStorage
) : ResourceAccessor {
    override suspend fun exists(path: Path) = storage.containsKey(path)
    override suspend fun readTextOrNull(path: Path, range: IntRange?) =  storage[path]
        ?.takeIf { range == null }
        ?.substring(range!!)
    override suspend fun readBytesOrNull(path: Path, range: IntRange?): ByteArray? = readTextOrNull(path, range)?.let(Base64::decode)
    override suspend fun <T> attach(path: Path, attach: suspend (String) -> T): T {
        requireExists(path)
        return attach(readText(path))
    }
    override fun toString() = "MemoryResourceAccessor"
}