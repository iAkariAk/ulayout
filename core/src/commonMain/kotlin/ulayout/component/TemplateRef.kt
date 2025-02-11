package com.akari.ulayout.component

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
@SerialName("@template")
data class TemplateRef(
    override val x: Int,
    override val y: Int,
    val name: String,
    val args: Map<String, JsonElement>,
) : Component()