plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.resources)
}


kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                }
                devtool = "source-map"
                sourceMaps = true
                outputFileName = "ulayout.js"
            }

        }
        binaries.executable()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.resources)
                implementation(libs.okio)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.html)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

