@file:OptIn(ExperimentalEncodingApi::class)

package com.akari.ulayout.resource

import com.akari.ulayout.graphics.DomImage
import com.akari.ulayout.graphics.ScaleDescription
import com.akari.ulayout.util.UlayoutJson
import com.akari.ulayout.util.suspendedLazy
import com.goncalossilva.resources.Resource
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

data class ImageResource(
    val image: DomImage,
    val scaleDescription: ScaleDescription? = null
)

suspend fun ByteArray.encodeToDomImage() = suspendCoroutine { continuation ->
    // 89504E470D0A1A0A: PNG's magic number
    check(
        this[0] == 0x89.toByte()
                && this[1] == 0x50.toByte()
                && this[2] == 0x4E.toByte()
                && this[3] == 0x47.toByte()
                && this[4] == 0x0D.toByte()
                && this[5] == 0x0A.toByte()
                && this[6] == 0x1A.toByte()
                && this[7] == 0x0A.toByte()
    ) {
        "Currently, only PNG is supported."
    }

    val base64 = Base64.encode(this)
    val image = DomImage().apply {
        src = "data:image/png;base64,$base64"
    }

    image.onload = {
        continuation.resume(image)
    }
    image.onerror = { _, cause, _, _, _ ->
        continuation.resumeWithException(IllegalStateException(cause))
    }
}

fun ImageResource(path: String) = suspendedLazy {
    suspendCoroutine { continuation ->


        val sdPath = "$path.sd.json"
        val description = try {
            val descriptionJson = Resource(sdPath).readText()
            UlayoutJson.decodeFromString<ScaleDescription>(descriptionJson)
        } catch (_: Throwable) {
            console.log("Cannot found scale description file $sdPath")
            null
        }

        val image = DomImage().apply {
            src = path
        }
        image.onload = {
            val res = ImageResource(image, description)
            continuation.resume(res)
        }
        image.onerror = { _, cause, _, _, _ ->
            continuation.resumeWithException(IllegalStateException(cause))
        }
    }
}

internal abstract class Resources(
    protected val pathPrefix: String = ""
) {
    protected fun image(path: String) = ImageResource("$pathPrefix$path")
}