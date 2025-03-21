package com.akari.ulayout.ulpack

import com.akari.ulayout.dom.DataSource
import com.akari.ulayout.resource.accessor.*
import com.akari.ulayout.util.UlayoutJson
import korlibs.io.file.std.openAsZip
import korlibs.io.stream.openAsync
import okio.Path
import okio.Path.Companion.toPath


class Ulpack private constructor(
    val configure: UlayoutConfigure,
    val manifest: Manifest?,
    private val delegatedRA: ResourceAccessor = ResourceAccessor,
) : ResourceAccessor by (BuiltinResourceAccessor + delegatedRA).catch() {
    companion object {
        fun wrap(
            configure: UlayoutConfigure,
            manifest: Manifest = Manifest(
                version = -1,
                author = "Akari",
                name = "wrap",
                description = "The ulpack is wrapped by Ulpack.wrap()",
            ),
            storageBuilder: MutableMap<Path, String>.() -> Unit = {}
        ) = Ulpack(
            configure = configure,
            manifest = manifest,
            delegatedRA = buildMap(storageBuilder).asMemoryRA()
        )

        suspend fun fromZip(
            domSource: DataSource
        ): Ulpack {
            val zip = domSource.read().openAsync().openAsZip()
            val zra = ZipResourceAccessor(zip)
            val configureJson = zra.readText("layout.json".toPath())
            val configure = UlayoutConfigure.parse(configureJson)
            val manifestJson = zra.readTextOrNull("manifest.json".toPath())
            val manifest = manifestJson?.let {
                UlayoutJson.decodeFromString<Manifest>(manifestJson)
            }

            return Ulpack(
                configure = configure,
                manifest = manifest,
                delegatedRA = zra
            )
        }
    }
}