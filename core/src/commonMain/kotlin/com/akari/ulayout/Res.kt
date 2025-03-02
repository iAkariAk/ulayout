package com.akari.ulayout

import com.akari.ulayout.resource.Resources
import com.akari.ulayout.template.TemplateStorage

// builtin resource
// assets/button/almanac.png
internal object Res : Resources("./assets/ulayout/") {
    val templates = json<TemplateStorage>("templates.json")
    val background = image("background.png")
    val imageAlmanacStoneTablet = image("game/AlmanacUI/image_almanac_stone_tablet.png")

    object Button : Resources(Res, "button") {
        val almanac = image("almanac.png")
        val dialog = image("dialog.png")
    }
}