package com.akari.ulayout.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

typealias TemplateStorage = Map<String, Template>

@Serializable
data class Template(
    val params: Map<String, Param> = emptyMap(),
    val expand: List<JsonObject> = emptyList()
)

@Serializable
data class Param(
    val type: ParamType,
    val default: JsonElement? = null
)

enum class ParamType(val displayName: String) {
    @SerialName("string")
    STRING("string"),

    @SerialName("number")
    NUMBER("number"),

    @SerialName("boolean")
    BOOLEAN("boolean"),

    @SerialName("object")
    OBJECT("object"),

    @SerialName("array")
    ARRAY("array"),

    @SerialName("null")
    NULL("null");

    companion object {
        fun fromElement(element: JsonElement) = when (element) {
            is JsonArray -> ARRAY
            is JsonObject -> OBJECT
            is JsonPrimitive -> when {
                element.isString -> STRING
                element == JsonNull -> NULL
                element.isNumber() -> NUMBER
                element.isBoolean() -> BOOLEAN
                else -> null
            }

            JsonNull -> NULL
            else -> null
        } ?: error("JsonLiteral is a deprecated API, it shouldn't be ues.") // JsonLiteral
    }
}

private fun JsonPrimitive.isNumber() = try {
    int
    long
    float
    double
    true
} catch (_: Throwable) {
    false
}

private fun JsonPrimitive.isBoolean() = try {
    boolean
    true
} catch (_: Throwable) {
    false
}