package com.akari.ulayout

import com.akari.ulayout.resource.accessor.ResourceAccessor
import com.akari.ulayout.template.TemplateProvider

internal class Environment(
    val templates: TemplateProvider,
    val resources: ResourceAccessor
)