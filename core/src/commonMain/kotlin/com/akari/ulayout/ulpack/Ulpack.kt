package com.akari.ulayout.ulpack

import com.akari.ulayout.resource.accessor.*
import okio.Path


class Ulpack private constructor(
    val configure: UlayoutConfigure,
    private val storage: ResourceStorage
) : ResourceAccessor by (BuiltinResourceAccessor + MemoryResourceAccessor(storage)).catch() {
    companion object {
        fun wrap(
            configure: UlayoutConfigure, storageBuilder: MutableMap<Path, String>.() -> Unit = {}
        ) = Ulpack(configure, buildMap(storageBuilder))
    }
}