package com.akari.ulayout.resource.accessor

import okio.Path
import okio.Path.Companion.toPath

private val BUILTIN_PATH = "./assets/ulayout/".toPath()
private const val BUILTIN_PREFIX = "@builtin"

object BuiltinResourceAccessor : ResourceAccessor {
    private val Path.actualPath: Path?
        get() {
            val shortPath = takeIf { it.segments.first() == BUILTIN_PREFIX }
                ?.relativeTo("@builtin".toPath()) ?: return null
            return (BUILTIN_PATH / shortPath)
        }

    override suspend fun exists(path: Path) = path.actualPath
        ?.let { ResourceAccessor.exists(it) } == true

    override suspend fun readTextOrNull(path: Path) = path.actualPath
        ?.let { ResourceAccessor.readTextOrNull(it) }

    override suspend fun readBytesOrNull(path: Path) = path.actualPath
        ?.let { ResourceAccessor.readBytesOrNull(it) }

    override suspend fun <T> attach(path: Path, attach: suspend (String) -> T): T {
        val actualPath = requireExists(path).actualPath!!
        return ResourceAccessor.attach(actualPath, attach)
    }

    override fun toString() = "BuiltinResourceAccessor"
}