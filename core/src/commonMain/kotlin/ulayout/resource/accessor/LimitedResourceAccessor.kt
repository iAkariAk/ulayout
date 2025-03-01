package ulayout.resource.accessor

import okio.Path
import ulayout.util.contains

class LimitedResourceAccessor(
    private val limit: Path,
    private val delegate: ResourceAccessor
) : ResourceAccessor by delegate {
    override fun readBytesOrNull(path: Path): ByteArray? {
        checkValid(path)
        return delegate.readBytesOrNull(path)
    }

    override fun readTextOrNull(path: Path): String? {
        checkValid(path)
        return delegate.readTextOrNull(path)
    }

    override fun exists(path: Path): Boolean {
        checkValid(path)
        return delegate.exists(path)
    }

    private fun checkValid(target: Path) {
        check(limit.contains(target)) {
            "Cannot access a limited parent path."
        }
    }

    override fun toString() = "LimitedResourceAccessor<$delegate>"
}

fun ResourceAccessor.limited(limit: Path) = LimitedResourceAccessor(
    limit, this
)
