package com.akari.ulayout.util

import com.akari.ulayout.graphics.Rect
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RectTest {
    @Test
    fun `test contains`() {
        val rect = Rect.makeXYWH(267.0, 384.0, 300.0, 100.0)
        assertTrue(rect.contains(267.0, 384.0))
        assertTrue(rect.contains(467.0, 404.0))
        assertTrue(rect.contains(Rect.makeXYWH(367.0, 404.0, 1.0, 10.0)))
        assertFalse(rect.contains(1.0, 1.0))
    }
}
