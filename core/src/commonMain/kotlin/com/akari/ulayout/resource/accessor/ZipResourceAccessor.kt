package com.akari.ulayout.resource.accessor

import com.akari.ulayout.util.normalizeToString
import kotlinx.coroutines.await
import okio.Path
import org.readium.jszip.JSZip

class ZipResourceAccessor(
    private val jszip: JSZip
) : ResourceAccessor {
    companion object {
        suspend fun open(bytes: ByteArray) = ZipResourceAccessor(
            JSZip().loadAsync(bytes).await()
        )
    }

    override fun exists(path: Path): Boolean =
        jszip.file(path.normalizeToString()) != null

    override fun readTextOrNull(path: Path): String? =
        jszip.file(path.normalizeToString())?.toString()

    override fun readBytesOrNull(path: Path): ByteArray? = TODO()
//        jszip.file(path.normalizeToString())?.async<String>("binarystring") { }?.let { bin ->
//            ByteArray(bin.length) {
//                bin[it].code.toUByte().toByte()
//            }
//        }
}