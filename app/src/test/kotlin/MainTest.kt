package com.akari.ulpackm

import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.test.Test

class MainTest {
    @Test
    fun `test app`() {
        val fs = FileSystem.RESOURCES
        encodeToStorage(fs, "/ulpack".toPath())
    }
}