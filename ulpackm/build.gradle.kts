plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)

    application
}

group = "$group.ulpackm"

application {
    mainClass = "com.akari.ulpackm.MainKt"
}

dependencies {
    implementation(libs.okio)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.kotlin.test)
}

kotlin {
    jvmToolchain(21)
}