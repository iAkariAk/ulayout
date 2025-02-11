package com.akari.ulayout.graphics

import kotlinx.coroutines.asDeferred
import org.w3c.dom.Document
import kotlin.js.Promise

data class Font(
    val family: String,
    val paths: List<String>
) {
    suspend fun attachTo(document: Document) {
        val promise = eval(
            """
            const font = new FontFace('ZhanKuKuaiLeTi2016', 'url(assets/ulayout/font/ZhanKuKuaiLeTi2016.woff2)')
            font.load().then(font => {
                document.fonts.add(font)
            })
        """.trimIndent()
        ) as Promise<dynamic>
        promise.asDeferred().await()
    }
}