package com.akari.ulpackm

import kotlinx.serialization.json.Json
import okio.BufferedSource
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.openZip

fun main(args: Array<String>) {
    val input = checkNotNull(args.getOrNull(0)) { "Missing arg: input" }.toPath()
    val output = checkNotNull(args.getOrNull(1)) { "Missing arg: output" }.toPath()

    val fs = FileSystem.SYSTEM
    val storage = fs.openZip(input).use { inputFS ->
        encodeToStorage(inputFS, "/".toPath())
    }
    fs.write(output) { writeUtf8(storage) }
}

internal fun encodeToStorage(fs: FileSystem, path: Path): String {
    val map = fs.listRecursively(path)
        .filterNot { fs.metadata(it).isDirectory }
        .associateWith { encodeToString(fs, it) }
        .mapKeys { (key, _) -> key.relativeTo(path).toString() }
    return Json.encodeToString(map)
}

private fun encodeToString(fs: FileSystem, path: Path): String {
    val name = path.name
    return when {
        name.endsWith(".png") -> {
            val byteString = fs.read(path, BufferedSource::readByteString)
            val magicNumber = byteString.substring(0, 8).hex()
            check(magicNumber.startsWith("89504E470D0A1A0A", true)) {
                "File isn't PNG, which magic number don't equal 0x89504E470D0A1A0A"
            }
            "data:image/png;base64,${byteString.base64()}"
        }

        else -> fs.read(path, BufferedSource::readUtf8)
    }
}