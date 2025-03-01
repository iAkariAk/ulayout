package org.readium.jszip

import kotlinx.coroutines.await

typealias OnUpdateCallback = (metadata: JSZipMetadata) -> Unit

suspend inline fun <reified T> JSZipObject.async(
    type: String,
    noinline onUpdate: OnUpdateCallback? = null
): T = this.async(type, onUpdate).await() as T

suspend fun JSZip.loadAsync(
    data: ByteArray,
    options: JSZipLoadOptions
): JSZip = this.loadAsync(data.unsafeCast<Any>(), options).await()

suspend fun JSZip.generateAsync(
    options: JSZipGeneratorOptions
): ByteArray = this.generateAsync(options).await() as ByteArray


typealias JSZipObjectOptions = JSZipFileOptions