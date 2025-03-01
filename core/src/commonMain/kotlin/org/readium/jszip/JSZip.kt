@file:JsModule("jszip")
@file:JsNonModule
@file:Suppress("unused", "ENUM_CLASS_IN_EXTERNAL_DECLARATION_WARNING")

package org.readium.jszip

import js.buffer.ArrayBuffer
import org.w3c.files.Blob
import kotlin.js.Date
import kotlin.js.Promise
import kotlin.js.RegExp

external interface JSZipSupport {
    val arraybuffer: Boolean
    val uint8array: Boolean
    val blob: Boolean
    val nodebuffer: Boolean
}

external enum class Compression {
    STORE,
    DEFLATE
}

external interface CompressionOptions {
    var level: Int
}


external interface InputByType {
    var base64: String
    var string: String
    var text: String
    var binarystring: String
    var array: Array<Int>

    //    var uint8array: Uint8Array
    var arraybuffer: ArrayBuffer
    var blob: Blob
    var stream: Any /* NodeJS.ReadableStream */
}

external interface OutputByType {
    var base64: String
    var string: String
    var text: String
    var binarystring: String
    var array: Array<Int>

    //    var uint8array: Uint8Array
    var arraybuffer: ArrayBuffer
    var blob: Blob
    var nodebuffer: Any /* Buffer */
}


@JsName("default")
external class JSZip {
    companion object {
        val support: JSZipSupport
        val external: dynamic
        val version: String
    }

    val files: dynamic

    fun file(path: String): JSZipObject?
    fun file(path: RegExp): Array<JSZipObject>
    fun file(path: String, data: Any?, options: JSZipFileOptions = definedExternally): JSZip
    fun folder(name: String): JSZip?
    fun folder(name: RegExp): Array<JSZipObject>
    fun forEach(callback: (relativePath: String, file: JSZipObject) -> Unit)
    fun filter(predicate: (relativePath: String, file: JSZipObject) -> Boolean): Array<JSZipObject>
    fun remove(path: String): JSZip

    fun generateAsync(
        options: JSZipGeneratorOptions = definedExternally,
        onUpdate: OnUpdateCallback? = definedExternally
    ): Promise<Any?>

    fun generateNodeStream(
        options: JSZipGeneratorOptions = definedExternally,
        onUpdate: OnUpdateCallback? = definedExternally
    ): Any /* NodeJS.ReadableStream */

    fun loadAsync(data: Any, options: JSZipLoadOptions = definedExternally): Promise<JSZip>
}

external interface JSZipObject {
    val name: String
    val unsafeOriginalName: String?
    val dir: Boolean
    val date: Date
    val comment: String
    val unixPermissions: Any? // Int | String
    val dosPermissions: Int?
    val options: JSZipObjectOptions

    fun async(type: String, onUpdate: OnUpdateCallback? = definedExternally): Promise<Any?>
    fun nodeStream(
        type: String = definedExternally,
        onUpdate: OnUpdateCallback? = definedExternally
    ): Any /* NodeJS.ReadableStream */
}

external interface JSZipFileOptions {
    var base64: Boolean?
    var binary: Boolean?
    var date: Date?
    var compression: Compression?
    var compressionOptions: CompressionOptions?
    var comment: String?
    var optimizedBinaryString: Boolean?
    var createFolders: Boolean?
    var dir: Boolean?
    var dosPermissions: Int?
    var unixPermissions: Any? // Int | String
}

external interface JSZipGeneratorOptions {
    var compression: Compression?
    var compressionOptions: CompressionOptions?
    var type: String?
    var comment: String?
    var mimeType: String?
    var encodeFileName: ((String) -> String)?
    var streamFiles: Boolean?
    var platform: String? // "DOS" | "UNIX"
}

external interface JSZipLoadOptions {
    var base64: Boolean?
    var checkCRC32: Boolean?
    var optimizedBinaryString: Boolean?
    var createFolders: Boolean?
    var decodeFileName: ((Any) -> String)? // Uint8Array | Buffer | Array<String>
}


external interface JSZipMetadata {
    val percent: Number
    val currentFile: String?
}

external interface JSZipStreamHelper<T> {
    fun on(event: String, callback: Any): JSZipStreamHelper<T>
    fun accumulate(updateCallback: ((JSZipMetadata) -> Unit)? = definedExternally): Promise<T>
    fun resume(): JSZipStreamHelper<T>
    fun pause(): JSZipStreamHelper<T>
}

