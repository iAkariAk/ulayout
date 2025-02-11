package com.akari.ulayout.template

import com.akari.ulayout.Res
import com.akari.ulayout.util.UlayoutJson

val BuiltinTemplates =
    UlayoutJson.decodeFromString<Map<String, Template>>(Res.templates)
