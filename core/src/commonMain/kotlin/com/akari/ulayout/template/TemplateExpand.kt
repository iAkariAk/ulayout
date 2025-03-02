package com.akari.ulayout.template

import com.akari.ulayout.component.Component
import com.akari.ulayout.component.TemplateRef
import com.akari.ulayout.component.VisualComponent
import com.akari.ulayout.util.UlayoutJson
import kotlinx.serialization.json.*


private fun Template.expandArgs(args: Map<String, JsonElement>): List<Map<String, JsonElement>> {
    fun translateArgOrSelf(value: JsonPrimitive): JsonElement {
        val content = value.content
        if (!content.startsWith("@args.")) return value
        val argName = content.removePrefix("@args.")
        val param = params[argName] ?: return value
        val arg = args[argName] ?: param.default
        checkNotNull(arg) { "$argName require value is must" }
        val actualType = ParamType.fromElement(arg)
        check(param.type == actualType) {
            "Cannot match same type in $argName($arg), expected type is ${param.type.displayName}, but actual type is ${actualType.displayName}"
        }
        return arg
    }

    fun JsonObject.expandArgs(): JsonObject =
        mapValues { (_, value) ->
            when (value) {
                is JsonPrimitive if value.isString -> translateArgOrSelf(value)
                else -> if (value is JsonObject) value.expandArgs() else value
            }
        }.let(::JsonObject)

    return expand.map { it.expandArgs() }
}

internal fun TemplateRef.expand(templateProvider: TemplateProvider): List<Component> {
    val template = templateProvider[name]
    return template?.expandArgs(args)?.map { obj ->
        obj.mapValues { (key, value) ->
            when (key) {
                "x" -> JsonPrimitive(x + value.jsonPrimitive.int)
                "y" -> JsonPrimitive(y + value.jsonPrimitive.int)
                else -> value
            }
        }.let(::JsonObject)
    }?.map { UlayoutJson.decodeFromJsonElement<Component>(it) }
        ?: error("Cannot expand because template $name not found")
}


internal fun List<Component>.expandAll(templateProvider: TemplateProvider): List<VisualComponent> =
    flatMap { component ->
        when (component) {
            is TemplateRef -> component.expand(templateProvider).expandAll(templateProvider)// recursive
            is VisualComponent -> listOf(component)
        }
    }
