package com.akari.ulayout.resource.accessor

import com.akari.ulayout.util.contains
import okio.Path

class LimitedResourceAccessor(
    private val limit: Path,
    private val delegate: ResourceAccessor
) : ResourceAccessor by delegate {
    override suspend fun readBytesOrNull(path: Path, range: IntRange?): ByteArray? {
        checkValid(path)
        return delegate.readBytesOrNull(path, range)
    }

    override suspend fun readTextOrNull(path: Path, range: IntRange?): String? {
        checkValid(path)
        return delegate.readTextOrNull(path, range)
    }

    override suspend fun exists(path: Path): Boolean {
        checkValid(path)
        return delegate.exists(path)
    }

    private fun checkValid(target: Path) {
        check(limit.contains(target)) {
            "Cannot access a limited parent path $target."
        }
    }

    override fun toString() = "LimitedResourceAccessor<$delegate>"
}

fun ResourceAccessor.limited(limit: Path) = LimitedResourceAccessor(
    limit, this
)
