package com.akari.ulayout.graphics

import kotlinx.serialization.Serializable
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.abs

@Serializable
data class Rect(
    var left: Double = 0.0,
    var top: Double = 0.0,
    var right: Double = 0.0,
    var bottom: Double = 0.0
) {
    companion object {
        fun makeXYWH(x: Double, y: Double, width: Double, height: Double) =
            Rect(x, y, x + width, y + height)

        fun ofPoint(x: Double, y: Double) = makeXYWH(x, y, 0.0, 0.0)
    }

    val width: Double get() = right - left
    val height: Double get() = bottom - top

    init {
        require(right.equalsApprox(left) || right > left) { "Rect width must be positive" }
        require(bottom.equalsApprox(top) || bottom > top) { "Rect height must be positive" }
    }

    fun contains(x: Double, y: Double) =
        x in left..right && y in top..bottom

    fun contains(other: Rect): Boolean {
        return (contains(other.left, other.top)
                && contains(other.right, other.bottom))
    }

    fun map(translate: (Double) -> Double) = copy(
        left = translate(left),
        right = translate(right),
        top = translate(top),
        bottom = translate(bottom)
    )

    fun translate(dx: Double, dy: Double) = copy(
        left = left + dx,
        right = right + dx,
        top = top + dy,
        bottom = bottom + dy,
    )
}

fun Double.equalsApprox(other: Double, epsilon: Double = 1e-5) =
    abs(this - other) < epsilon


val HTMLCanvasElement.bounds: Rect
    get() = Rect.makeXYWH(0.0, 0.0, width.toDouble(), height.toDouble())

fun CanvasRenderingContext2D.rect(rect: Rect) = rect(rect.left, rect.top, rect.width, rect.height)