package com.akari.ulayout.template

internal class TemplateProvider {
    private val storage = mutableMapOf<String, Template>()

    fun put(name: String, template: Template) {
        storage.put(name, template)
    }

    fun putAll(map: Map<String, Template>) {
        storage.putAll(map)
        storage += emptyMap()
    }

    operator fun plusAssign(map: Map<String, Template>) = putAll(map)
    operator fun get(name: String): Template? = storage.get(name)
}