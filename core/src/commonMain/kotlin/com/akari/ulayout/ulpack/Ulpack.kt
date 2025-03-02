package com.akari.ulayout.ulpack

import com.akari.ulayout.dom.DataSource
import com.akari.ulayout.resource.accessor.*
import korlibs.io.file.std.openAsZip
import korlibs.io.stream.openAsync
import okio.Path
import okio.Path.Companion.toPath


class Ulpack private constructor(
    val configure: UlayoutConfigure,
    private val delegatedRA: ResourceAccessor = ResourceAccessor,
) : ResourceAccessor by (BuiltinResourceAccessor + delegatedRA).catch() {
    companion object {
        fun wrap(
            configure: UlayoutConfigure,
            storageBuilder: MutableMap<Path, String>.() -> Unit = {}
        ) = Ulpack(configure, buildMap(storageBuilder).asMemoryRA())

        suspend fun fromZip(
            domSource: DataSource
        ): Ulpack {
            val zip = domSource.read().openAsync().openAsZip()
            val zra = ZipResourceAccessor(zip)
            val configureJson = zra.readText("layout.json".toPath())
            val configure = UlayoutConfigure.parse(configureJson)
            return Ulpack(
                configure,
                zra
            )
        }
    }
}