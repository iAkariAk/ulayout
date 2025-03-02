package com.akari.ulayout.resource.accessor

import com.akari.ulayout.resource.encodeToBase64
import com.akari.ulayout.util.normalizeToString
import korlibs.io.file.VfsFile
import korlibs.io.file.std.openAsZip
import korlibs.io.stream.openAsync
import okio.ByteString.Companion.toByteString
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

    override suspend fun <T> attach(path: Path, attach: suspend (String) -> T): T {
        requireExists(path)
        val bytes = readBytes(path)
        val mineType = bytes.getMimeType()
        val data = bytes.encodeToBase64()
        return attach(data)
    }

    override fun toString() = "ZipResourceAccessor"
}

private fun ByteArray.getMimeType(): String {
    val bs = toByteString()

    val magic8byte = bs.substring(0, 3).hex()
    fun startsWith(prefix: String) = magic8byte.startsWith(prefix, true)
    return when {
        startsWith("ffd8ff") -> "image/jpeg"
        startsWith("89504e470d0a1a0a") -> "image/png"
        startsWith("474946") -> "image/gif"
        startsWith("49492a") -> "image/tiff"
        startsWith("4d4d002a") -> "image/tiff"
        startsWith("424d") -> "image/bmp"
        startsWith("52494646") -> "image/webp"
        else -> "application/octet-stream"
    }
}