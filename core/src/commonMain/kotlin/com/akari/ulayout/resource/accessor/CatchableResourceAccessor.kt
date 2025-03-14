package com.akari.ulayout.resource.accessor

import okio.Path

class CatchableResourceAccessor(
    private val delegate: ResourceAccessor
) : ResourceAccessor {
    private val catchesForExists = mutableMapOf<Path, Boolean>()
    private val catchesForText = mutableMapOf<Path, String?>()
    private val catchesForBytes = mutableMapOf<Path, ByteArray?>()
    private val catchesForAttach = mutableMapOf<Path, Any?>()

    override suspend fun exists(path: Path) = catchesForExists.getOrPut(path) {
        delegate.exists(path)
    }

    override suspend fun readTextOrNull(path: Path, range: IntRange?) = catchesForText.getOrPut(path) {
        delegate.readTextOrNull(path, range)
    }

    override suspend fun readBytesOrNull(path: Path, range: IntRange?): ByteArray? = catchesForBytes.getOrPut(path) {
        delegate.readBytesOrNull(path, range)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> attach(path: Path, attach: suspend (String) -> T) = catchesForAttach.getOrPut(path) {
        delegate.attach(path, attach)
    } as T

    override fun toString() = "CatchableResourceAccessor<$delegate>"
}

fun ResourceAccessor.catch() = CatchableResourceAccessor(this)