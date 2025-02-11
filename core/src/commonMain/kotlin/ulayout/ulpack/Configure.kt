package com.akari.ulayout.ulpack

import com.akari.ulayout.component.Component
import com.akari.ulayout.template.Template
import com.akari.ulayout.template.TemplateProvider
import com.akari.ulayout.template.expandAll
import com.akari.ulayout.util.UlayoutJson
import kotlinx.serialization.Serializable

@Serializable
data class UlayoutConfigure(
    val style: Style = Style.Default,
    val routes: Map<String, List<Component>> = emptyMap(),
    val templates: Map<String, Template> = emptyMap(),
    val common: List<Component>
) {
    companion object {
        fun parse(json: String) = UlayoutJson.decodeFromString<UlayoutConfigure>(json)
    }

    fun expand() {
        val provider = TemplateProvider()
        provider += templates
        UlayoutConfigure(
            style = style,
            routes = routes.mapValues { (_, value) -> value.expandAll(provider) },
            templates = templates,
            common = common.expandAll(provider)
        )
    }
}

@Serializable
data class Style(
    val background: String = "#f7ecdd",
) {
    companion object {
        val Default = Style()
    }
}

