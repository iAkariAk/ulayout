package com.akari.ulayout.component

import com.akari.ulayout.graphics.ScaleDescription
import com.akari.ulayout.graphics.drawImageWithScale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Image as DomImage

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
        val srcData = checkNotNull(resources[src]) { "Resource $src not found" }
        val image = DomImage()
        image.src = srcData
        image.onload = {
            context.drawImageWithScale(
                image,
                bounds,
                scale
            )
        }
    }
}
