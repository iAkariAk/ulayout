package com.akari.ulayout.resource.accessor

import com.akari.ulayout.util.normalizeToString
import korlibs.io.file.VfsFile
import korlibs.io.file.std.openAsZip
import korlibs.io.stream.openAsync
import okio.Path

class ZipResourceAccessor(
    private val vfs: VfsFile
) : ResourceAccessor {
    companion object {
        suspend fun open(bytes: ByteArray): ZipResourceAccessor {
            return ZipResourceAccessor(
                bytes.openAsync().openAsZip()
            )
        }
    }

    override suspend fun exists(path: Path): Boolean =
        vfs[path.normalizeToString()].exists()

    override suspend fun readTextOrNull(path: Path): String? =
        vfs[path.normalizeToString()].takeIf { it.exists() }?.readString()

    override suspend fun readBytesOrNull(path: Path): ByteArray? =
        vfs[path.normalizeToString()].takeIf { it.exists() }?.readBytes()
}