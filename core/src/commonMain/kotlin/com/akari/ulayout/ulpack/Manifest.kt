package com.akari.ulayout.ulpack

import kotlinx.serialization.Serializable

@Serializable
data class Manifest(
    val version: Int,
    val author: String,
    val name: String,
    val description: String
)