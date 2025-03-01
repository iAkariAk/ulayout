package com.akari.ulayout.graphics

import kotlinx.coroutines.asDeferred
import org.w3c.dom.Document
import kotlin.js.Promise

data class Font(
    val family: String,
    val paths: List<String>
) {
    suspend fun attachTo(document: Document) {
        val src = paths.joinToString(",") { path ->
            val suffix = path.substringAfterLast(".")
            val format = when (suffix) {
                "woff2" -> "woff2"
                "woff" -> "woff"
                "eot" -> "embedded-opentype"
                "svg" -> "svg"
                "ttf" -> "truetype"
                "otf" -> "opentype"
                else -> throw IllegalArgumentException("Unsupported font format: $suffix")
            }
            """url("$path") format("$format")"""
        }
        val promise = eval(
            """
            const font = new FontFace('$family', '$src')
            font.load().then(font => {
                document.fonts.add(font)
            })
        """.trimIndent() // kotlin-wrappers make dist very large
        ) as Promise<dynamic>
        promise.asDeferred().await()
    }
}