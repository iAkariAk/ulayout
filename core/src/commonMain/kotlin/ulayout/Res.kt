package com.akari.ulayout

import com.akari.ulayout.resource.Resources

// builtin resource
// assets/button/almanac.png
internal object Res : Resources("/assets/ulayout/") {
    val background = image("background.png")

    object Button : Resources("${pathPrefix}button/") {
        val almanac = image("almanac.png")
        val dialog = image("dialog.png")
    }
}