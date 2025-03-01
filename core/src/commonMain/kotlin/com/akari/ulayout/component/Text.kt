package com.akari.ulayout.component

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextBaseline
import org.w3c.dom.TOP

@Serializable
@SerialName("Text")
data class Text(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    override val events: Map<String, String> = emptyMap(),
    val text: String,
    val textColor: String = "#0a84da",
    val textSize: Int = 20,
    val textFont: String = "ZhanKuKuaiLeTi2016"
) : VisualComponent() {
    override suspend fun paint(context: CanvasRenderingContext2D) {
        super.paint(context)
        context.paintToDst { dst ->
            fillStyle = textColor
            font = "${textSize}px $textFont"

            textBaseline = CanvasTextBaseline.TOP
            fillText(text, dst.left, dst.top , dst.width)
        }
    }
}
