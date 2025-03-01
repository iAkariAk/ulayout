package ulayout

import com.akari.ulayout.template.TemplateProvider
import ulayout.resource.accessor.ResourceAccessor

class Environment(
    val templates: TemplateProvider,
    val resources: ResourceAccessor
)