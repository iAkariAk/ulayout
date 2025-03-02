import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotinx.atomicfu) apply false
    alias(libs.plugins.resources) apply false
}

subprojects {
    group = "com.akari.ulpack"
    version = "0.0.0-SHOTSNAP"

    if (name == "ulpackm") {
        apply(plugin = "org.jetbrains.kotlin.jvm")
    } else {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
    }
    tasks.withType<AbstractKotlinCompile<*>> {
        compilerOptions.optIn = listOf(
            "kotlinx.serialization.ExperimentalSerializationApi",
            "InternalSerializationApi"
        )
        compilerOptions.freeCompilerArgs.addAll(
            "-Xwhen-guards",
            "-Xnon-local-break-continue",
            "-Xmulti-dollar-interpolation",
        )
    }

    tasks.withType<KotlinJsCompile> {
        compilerOptions {
            target = "es2015"
            useEsClasses = true
        }
    }
}

