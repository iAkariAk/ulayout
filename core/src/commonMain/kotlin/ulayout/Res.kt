package com.akari.ulayout

import com.akari.ulayout.resource.Resources

// builtin resource
// assets/button/almanac.png
internal object Res : Resources("/assets/ulayout/") {
    val templates = text("templates.json")
    val background = image("background.png")
    val sss = image("game/AlmanacUI/image_almanac_stone_tablet.png")

    object Button : Resources(Res, "button") {
        val almanac = image("almanac.png")
        val dialog = image("dialog.png")
    }
}