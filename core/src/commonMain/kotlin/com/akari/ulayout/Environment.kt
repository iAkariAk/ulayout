package com.akari.ulayout

import com.akari.ulayout.resource.accessor.ResourceAccessor
import com.akari.ulayout.template.TemplateProvider

class Environment(
    val templates: TemplateProvider,
    val resources: ResourceAccessor
)