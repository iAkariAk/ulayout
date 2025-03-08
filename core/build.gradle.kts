plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotinx.atomicfu)
}


kotlin {
    js(IR) {
        browser {
            useEsModules()
            useCommonJs()

            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        binaries.library()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.okio)
                api(libs.kotlinx.coroutines)
                api(libs.kotlinx.html)
                api(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.atomicfu.runtime)
                implementation(libs.korlibs.io)
                implementation(libs.jetbrains.annonations)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}



