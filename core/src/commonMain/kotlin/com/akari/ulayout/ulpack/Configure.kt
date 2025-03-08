package com.akari.ulayout.ulpack

import com.akari.ulayout.component.Component
import com.akari.ulayout.template.TemplateProvider
import com.akari.ulayout.template.TemplateStorage
import com.akari.ulayout.template.expandAll
import com.akari.ulayout.util.UlayoutJson
import kotlinx.serialization.Serializable

object DefaultsConfigure {
    val background = "@builtin/background.png"
}

@Serializable
data class UlayoutConfigure(
    val style: Style = Style.Default,
    val initRoute: String? = null,
    val routes: @Serializable(MapKeyPropagatingSerializer::class) Map<String, Route> = emptyMap(),
    val templates: TemplateStorage = emptyMap(),
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
            routes = routes.mapValues { (_, value) -> value.expand(provider) },
            templates = templates,
            common = common.expandAll(provider)
        )
    }
}

@Serializable
data class Style(
    val background: String = DefaultsConfigure.background,
) {
    companion object {
        val Default = Style()
    }
}

@Serializable
data class Route(
    @KeyInjured
    val name: String,
    val background: String = DefaultsConfigure.background,
    val components: List<Component>
) {
    internal fun expand(templateProvider: TemplateProvider) = copy(
        components = components.expandAll(templateProvider)
    )
}