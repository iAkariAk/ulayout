package com.akari.ulayout.graphics

import com.akari.ulayout.dom.DomImage
import kotlinx.serialization.Serializable
import org.w3c.dom.CanvasRenderingContext2D

@Serializable
data class ScaleDescription(
    val startX: Double,
    val startY: Double,
    val endX: Double,
    val endY: Double,
) {
    val contentRect get() = Rect(startX, startY, endX, endY)
}

fun CanvasRenderingContext2D.drawImageWithScale(
    image: DomImage,
    dst: Rect,
    description: ScaleDescription? = null
) {
    if (description == null) {
        drawImage(image, dst.left, dst.top, dst.width, dst.height)
        return
    }
    with(description) {
        // 角
        // 左上
        drawImage(
            image,
            0.0,
            0.0,
            startX,
            startY,
            dst.left,
            dst.top,
            startX,
            startY
        )
        // 右上
        drawImage(
            image,
            endX,
            0.0,
            image.width - endX,
            startY,
            dst.right - (image.width - endX),
            dst.top,
            image.width - endX,
            startY
        )
        // 左下
        drawImage(
            image,
            0.0,
            endY,
            startX,
            image.height - endY,
            dst.left,
            dst.bottom - (image.height - endY),
            startX,
            image.height - endY
        )
        //右下
        drawImage(
            image,
            endX,
            endY,
            image.width - endX,
            image.height - endY,
            dst.right - (image.width - endX),
            dst.bottom - (image.height - endY),
            image.width - endX,
            image.height - endY
        )

        // 边
        // 左
        drawImage(
            image,
            0.0,
            startY,
            startX,
            image.height - (startY + image.height - endY),
            dst.left,
            dst.top + startY,
            startX,
            dst.height - (startY + image.height - endY)
        )
        // 右
        drawImage(
            image,
            endX,
            startY,
            startX,
            image.height - (startY + image.height - endY),
            dst.right - (image.width - endX),
            dst.top + startY,
            startX,
            dst.height - (startY + image.height - endY)
        )
        // 上
        drawImage(
            image,
            startX,
            0.0,
            image.width - (startX + image.height - endY),
            startY,
            dst.left + startX,
            dst.top,
            dst.width - (startX + image.width - endX),
            startY
        )
        // 下
        drawImage(
            image,
            startX,
            endY,
            image.width - (startX + image.height - endY),
            startY,
            dst.left + startX,
            dst.bottom - (image.height - endY),
            dst.width - (startX + image.width - endX),
            startY
        )

        // 中心
        drawImage(
            image,
            startX,
            startY,
            endX - startX,
            endY - startY,
            dst.left + startX,
            dst.top + startY,
            dst.width - (startX + image.width - endX),
            dst.height - (startY + image.height - endY)
        )
    }
}