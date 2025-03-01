package com.akari.ulayout.util

import com.goncalossilva.resources.Resource

fun Resource.readTextOrNull() = try {
    readText()
} catch (_: Throwable) {
    null
}

fun Resource.readBytesOrNull() = try {
    readBytes()
} catch (_: Throwable) {
    null
}