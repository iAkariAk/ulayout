plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.resources)
}

group = "$group.app"

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                }
                devtool = "source-map"
                sourceMaps = true
                outputFileName = "ulevelApp.js"
            }
        }
        binaries.executable()


        val main by compilations.getting
        val test by compilations.getting
        listOf(main,test).forEach {  compilation->
            project.tasks.named<Copy>(compilation.processResourcesTaskName) {
                println("COPYYYY")
                from(project(":core").projectDir.resolve("src/commonMain/resources").also {
                    println(it)
                    println(it.list().contentToString())
                })
//            into("$projectDir/build/processedResources/js/main")
            }
        }

    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}


