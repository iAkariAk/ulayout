package com.akari.ulayout.util

import org.w3c.xhr.XMLHttpRequest

internal value class HttpUrl(val url: String) {
    fun get(config: (XMLHttpRequest.() -> Unit) = {}) = XMLHttpRequest().apply {
        open("GET", url, false)
        config()
        send()
    }

    fun exists() = get().exists()

    fun readTextOrNull(range: IntRange? = null) =
        get {
            range?.let {
                setRequestHeader("Range", "bytes=${range.first}-${range.last}")
            }
        }.takeIf { it.exists() }
            ?.responseText


    fun readBytesOrNull(range: IntRange? = null) =
        get {
            overrideMimeType("text/plain; charset=x-user-defined")
            range?.let {
                setRequestHeader("Range", "bytes=${range.start}-${range.endInclusive}")
            }
        }.takeIf { it.exists() }
            ?.let { response ->
                val raw = response.responseText
                ByteArray(raw.length) { raw[it].code.toUByte().toByte() }
            }
}

internal fun String.toHttpUrl() = HttpUrl(this)

internal fun XMLHttpRequest.exists() = status in 200..299

