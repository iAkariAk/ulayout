package com.akari.ulayout.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject


internal val UlayoutJson = Json {
    ignoreUnknownKeys = true
}

internal fun Map<String, JsonElement>.toJsonObject() = let(::JsonObject)