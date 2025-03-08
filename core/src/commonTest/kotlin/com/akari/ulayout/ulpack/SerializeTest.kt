package com.akari.ulayout.ulpack

import com.akari.ulayout.util.UlayoutJson
import kotlinx.serialization.Serializable
import org.intellij.lang.annotations.Language
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
private data class TestUser(
    @KeyInjured
    val key: String,
    val name: String
)

private typealias InjuredTestUserMap = @Serializable(MapKeyPropagatingSerializer::class) Map<String, TestUser>


class SerializeTest {
    @Test
    fun testInjure() {
        @Language("json")
        val json = """
            {
             "Jellyfish": {
               "name": "Jelee"
              },
              "Kiui": {
                "name": "Watase"
              }
            }
        """.trimIndent()
        val map = UlayoutJson.decodeFromString<InjuredTestUserMap>(json)
        assertEquals("Jelle", map["Jellyfish"]!!.key)
        assertEquals("Kiui", map["Watase"]!!.key)
    }
}