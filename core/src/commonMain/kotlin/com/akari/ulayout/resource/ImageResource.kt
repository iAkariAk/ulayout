@file:OptIn(ExperimentalEncodingApi::class)

package com.akari.ulayout.resource

import com.akari.ulayout.dom.DomImage
import com.akari.ulayout.graphics.ScaleDescription
import com.akari.ulayout.resource.accessor.ResourceAccessor
import com.akari.ulayout.resource.accessor.attachTo
import com.akari.ulayout.util.UlayoutJson
import com.akari.ulayout.util.appendSuffix
import com.akari.ulayout.util.suspendedLazy
import okio.Path
import okio.Path.Companion.toPath
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

data class ImageResource(
    val image: DomImage,
    val scaleDescription: ScaleDescription? = null
) {
    companion object
}

internal fun ByteArray.encodeToBase64(): String {
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
    return "data:image/png;base64,$base64"
}

internal suspend fun ByteArray.encodeToDomImage() = suspendCoroutine { continuation ->
    val image = DomImage().apply {
        src = encodeToBase64()
    }

    image.onload = {
        continuation.resume(image)
    }
    image.onerror = { _, cause, _, _, _ ->
        continuation.resumeWithException(IllegalStateException(cause))
    }
}

internal fun ResourceAccessor.getImage(path: Path) = suspendedLazy {
    ImageResource(
        image = attachTo(path, DomImage()),
        scaleDescription = readTextOrNull(path.appendSuffix(".sd.json"))?.let {
            UlayoutJson.decodeFromString(it)
        }
    )
}

internal suspend fun ImageResource.Companion.fromAssets(
    imagePath: String,
    descriptionPath: String = "$imagePath.sd.json"
) = fromData(
    imageSrcData = imagePath,
    descriptionJson = ResourceAccessor.readTextOrNull(descriptionPath.toPath())
)

internal fun ImageResource.Companion.fromData(
    imageSrcData: String,
    descriptionJson: String?,
) = suspendedLazy<ImageResource> {
    suspendCoroutine { continuation ->
        val description = descriptionJson?.let {
            UlayoutJson.decodeFromString<ScaleDescription>(it)
        }
        val image = DomImage().apply {
            src = imageSrcData
        }
        image.onload = {
            val res = ImageResource(image, description)
            continuation.resume(res)
        }
        image.onerror = { cause, file, line, column, g ->
            console.error("$file($line:$column): $cause")
            continuation.resumeWithException(IllegalStateException(cause.toString()))
        }
    }
}
