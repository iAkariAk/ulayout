package com.akari.ulayout.ulpack

import com.akari.ulayout.resource.*
import kotlinx.serialization.json.Json


class Ulpack private constructor(
    val configure: UlayoutConfigure,
    private val storage: ResourceStorage
) : ResourceProvider by CatchableResourceProvider(
    BuiltinResourceProvider()
            + MemoryResourceProvider(storage)
) {
    companion object {
        fun fromJson(json: String): Ulpack {
            val storage = Json.decodeFromString<ResourceStorage>(json)
            val configureJson = requireNotNull(storage["configure_old.json"])
            return Ulpack(
                configure = UlayoutConfigure.parse(configureJson),
                storage = storage
            )
        }

        fun wrap(
            configure: UlayoutConfigure,
            storageBuilder: MutableMap<String, String>.() -> Unit = {}
        ) =
            Ulpack(configure, buildMap(storageBuilder))
    }

}