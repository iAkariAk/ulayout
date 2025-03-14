package com.akari.ulayout.resource.accessor

import okio.Path

class CombinedResourceAccessor(
    private val inner: ResourceAccessor,
    private val outer: ResourceAccessor
) : ResourceAccessor {
    override suspend fun exists(path: Path) =
        inner.exists(path) || outer.exists(path)

    override suspend fun readTextOrNull(path: Path, range: IntRange?) =
        inner.readTextOrNull(path, range) ?: outer.readTextOrNull(path, range)

    override suspend fun readBytesOrNull(path: Path, range: IntRange?): ByteArray? =
        inner.readBytesOrNull(path, range) ?: outer.readBytesOrNull(path, range)

    override suspend fun <T> attach(path: Path, attach: suspend (String) -> T) =
        if (inner.exists(path)) {
            inner
        } else {
            outer
        }.attach(path, attach)

    override fun toString() = "CombinedResourceAccessor<$inner + $outer>"
}

operator fun ResourceAccessor.plus(other: ResourceAccessor) =
    CombinedResourceAccessor(this, other)
