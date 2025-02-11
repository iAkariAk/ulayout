package com.akari.ulayout.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
data class Template(
    val params: Map<String, Param> = emptyMap(),
    val expand: List<JsonObject> = emptyList()
)

@Serializable
data class Param(
    val type: ParamType,
    val default: JsonElement
)

enum class ParamType(val displayName: String) {
    @SerialName("string")
    STRING("string"),

    @SerialName("primitive")
    PRIMITIVE("primitive"),

    @SerialName("object")
    OBJECT("object"),

    @SerialName("array")
    ARRAY("array"),

    @SerialName("string?")
    NULLABLE_STRING("string?"),

    @SerialName("primitive?")
    NULLABLE_PRIMITIVE("primitive?"),

    @SerialName("object?")
    NULLABLE_OBJECT("object?"),

    @SerialName("array?")
    NULLABLE_ARRAY("array?");

    companion object {
        fun fromElement(element: JsonElement) = when {
            element is JsonPrimitive && element.isString -> STRING
            element is JsonPrimitive && !element.isString -> PRIMITIVE
            element is JsonObject -> OBJECT
            element is JsonArray -> ARRAY
            element is JsonNull || element is JsonPrimitive && element.isString -> NULLABLE_STRING
            element is JsonNull || element is JsonPrimitive && !element.isString -> NULLABLE_PRIMITIVE
            element is JsonNull || element is JsonObject -> NULLABLE_OBJECT
            element is JsonNull || element is JsonArray -> NULLABLE_ARRAY
            else -> error("JsonLiteral is a deprecated API, it shouldn't be ues.") // JsonLiteral
        }
    }
}
