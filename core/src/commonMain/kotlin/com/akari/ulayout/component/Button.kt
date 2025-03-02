package com.akari.ulayout.component

import com.akari.ulayout.Res
import com.akari.ulayout.graphics.drawImageWithScale
import com.akari.ulayout.resource.ImageResource
import com.akari.ulayout.util.SuspendedProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextBaseline
import org.w3c.dom.TOP

@Serializable
@SerialName("Button")
data class Button(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    override val events: Map<String, String> = emptyMap(),
    val text: String,
    val style: ButtonStyle = ButtonStyle.Almanac,
    val textColor: String = "#f0f2c5",
    val textSize: Int = 32,
    val textFont: String = "ZhanKuKuaiLeTi2016"
) : VisualComponent() {

    override suspend fun paint(context: CanvasRenderingContext2D) {
        super.paint(context)
        context.paintToDst { dst ->
            val (image, sd) = style.res.get()
            drawImageWithScale(image, dst, sd)
            val contentRect = (sd?.contentRect ?: dst).translate(x.toDouble(), y.toDouble())
            fillStyle = textColor
            font = "${textSize}px $textFont"
            textBaseline = CanvasTextBaseline.TOP
            fillText(text, contentRect.left, contentRect.top, contentRect.width)
        }
    }
}

enum class ButtonStyle(
    internal val res: SuspendedProvider<ImageResource>
) {
    @SerialName("almanac")
    Almanac(Res.Button.almanac),

    @SerialName("dialog")
    Dialog(Res.Button.dialog);
}
