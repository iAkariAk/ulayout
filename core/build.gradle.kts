import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    kotlin("multiplatform") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("com.goncalossilva.resources") version "0.10.0"
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
                implementation("com.goncalossilva:resources:0.10.0")
                implementation("com.squareup.okio:okio:3.10.2")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.12.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

kotlin {
    compilerOptions {
        optIn = listOf(
            "kotlinx.serialization.ExperimentalSerializationApi",
            "InternalSerializationApi"
        )
        freeCompilerArgs.addAll(
            "-Xwhen-guards",
            "-Xnon-local-break-continue",
            "-Xmulti-dollar-interpolation",
        )
    }
}

repositories {
    mavenCentral()
}

tasks.withType<KotlinJsCompile>().configureEach {
    compilerOptions {
        target = "es2015"
    }
}

