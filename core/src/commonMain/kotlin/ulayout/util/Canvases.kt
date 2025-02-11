package com.akari.ulayout.util

import org.w3c.dom.CanvasRenderingContext2D

fun CanvasRenderingContext2D.measureTextHeight(text: String, maxWidth: Double): Double {
    // 使用标准字符（如"A"）获取字体行高
    val metrics = measureText("A")
    val lineHeight = metrics.actualBoundingBoxAscent + metrics.actualBoundingBoxDescent

    var lineCount = 0
    var currentLine = StringBuilder()

    text.forEach { char ->
        // 先构建临时行测试宽度
        val tempLine = currentLine.toString() + char
        val tempWidth = measureText(tempLine).width

        if (tempWidth > maxWidth) {
            // 超出行宽时换行
            lineCount++
            currentLine.clear()
        }
        currentLine.append(char)
    }

    // 处理最后一行
    if (currentLine.isNotEmpty()) lineCount++

    return lineCount * lineHeight
}