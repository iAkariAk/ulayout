package com.akari.ulayout.resource.accessor

import okio.Path

class CombinedResourceAccessor(
    private val inner: ResourceAccessor,
    private val outer: ResourceAccessor
) : ResourceAccessor {
    override fun exists(path: Path) =
        inner.exists(path) || outer.exists(path)

    override fun readTextOrNull(path: Path) =
        inner.readTextOrNull(path) ?: outer.readTextOrNull(path)

    override fun readBytesOrNull(path: Path) =
        inner.readBytesOrNull(path) ?: outer.readBytesOrNull(path)

    override suspend fun <T> attach(path: Path, attach: suspend (String) -> T) =
        if (inner.exists(path)) {
            inner
        } else {
            println(outer)

            outer
        }.attach(path, attach)

    override fun toString() = "CombinedResourceAccessor<$inner + $outer>"
}

operator fun ResourceAccessor.plus(other: ResourceAccessor) =
    CombinedResourceAccessor(this, other)
