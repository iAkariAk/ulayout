package com.akari.ulayout.resource.accessor

import okio.Path.Companion.toPath

val BuiltinResourceAccessor = AliasResourceAccessor(
    "builtin",
    "./assets/ulayout/".toPath()
)