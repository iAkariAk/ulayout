package com.akari.ulayout.ulpack

import okio.Path
import ulayout.resource.accessor.*


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