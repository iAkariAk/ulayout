package com.akari.ulayout.util

import org.w3c.xhr.XMLHttpRequest

internal value class HttpUrl(val url: String) {
    fun get(config: (XMLHttpRequest.() -> Unit) = {}) = XMLHttpRequest().apply {
        open("GET", url, false)
        config()
        send()
    }

    fun exists() = get().exists()

    fun readTextOrNull() =
        get()
            .takeIf { it.exists() }
            ?.responseText


    fun readBytesOrNull() =
        get {
            overrideMimeType("text/plain; charset=x-user-defined")
        }.takeIf { it.exists() }
            ?.let { response ->
                val raw = response.responseText
                ByteArray(raw.length) { raw[it].code.toUByte().toByte() }
            }
}

internal fun String.toHttpUrl() = HttpUrl(this)

internal fun XMLHttpRequest.exists() = status in 200..299

