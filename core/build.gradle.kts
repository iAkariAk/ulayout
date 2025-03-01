plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.resources)
}


kotlin {
    js(IR) {
        browser {
            useEsModules()
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.resources)
                api(libs.okio)
                api(libs.kotlinx.coroutines)
                api(libs.kotlinx.html)
                api(libs.kotlinx.serialization.json)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

