package com.akari.ulayout.resource.accessor

import okio.Path
import okio.Path.Companion.toPath

class AliasResourceAccessor(
    val alias: String,
    private val target: Path,
) : ResourceAccessor {
    private val Path.actualPath: Path?
        get() {
            val firstSegment = "@$alias"
            val shortPath = takeIf { it.segments.first() == firstSegment }
                ?.relativeTo(firstSegment.toPath()) ?: return null
            return (target / shortPath)
        }
    private val ra = ResourceAccessor.limited(target)

    override suspend fun exists(path: Path) = path.actualPath
        ?.let { ra.exists(it) } == true

    override suspend fun readTextOrNull(path: Path) = path.actualPath
        ?.let { ra.readTextOrNull(it) }

    override suspend fun readBytesOrNull(path: Path) = path.actualPath
        ?.let { ra.readBytesOrNull(it) }

    override suspend fun <T> attach(path: Path, attach: suspend (String) -> T): T {
        val actualPath = requireExists(path).actualPath!!
        return ra.attach(actualPath, attach)
    }

    override fun toString() = "AliasResourceAccessor($alias)"
}