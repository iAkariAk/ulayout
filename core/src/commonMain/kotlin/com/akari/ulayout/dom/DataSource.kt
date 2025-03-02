package com.akari.ulayout.dom

import com.akari.ulayout.util.HttpUrl
import com.akari.ulayout.util.toHttpUrl
import org.w3c.xhr.XMLHttpRequest

sealed interface DataSource {
    fun read(): ByteArray
}

class UrlSource internal constructor(
    private val url: HttpUrl
) : DataSource {
    constructor(url: String) : this(url.toHttpUrl())

    private fun fetch(url: String) = XMLHttpRequest().apply {
        open("GET", url, false)
        overrideMimeType("text/plain; charset=x-user-defined")
        send()
    }

    override fun read() = checkNotNull(url.readBytesOrNull()) {
        "Failed to fetch $url, it may be not exists"
    }
}
