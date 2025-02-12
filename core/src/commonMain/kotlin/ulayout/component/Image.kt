package com.akari.ulayout.component

import com.akari.ulayout.graphics.ScaleDescription
import com.akari.ulayout.graphics.drawImageWithScale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.w3c.dom.CanvasRenderingContext2D

@Serializable
@SerialName("Image")
data class Image(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    override val events: Map<String, String> = emptyMap(),
    val scale: ScaleDescription? = null,
    val src: String,
) : VisualComponent() {
    override suspend fun paint(context: CanvasRenderingContext2D) {
        super.paint(context)
        val res = checkNotNull(resources.getImage(src)) {
            "Image resource $src not found"
        }
        context.drawImageWithScale(
            res.image,
            bounds,
            scale ?: res.scaleDescription
        )
    }
}
