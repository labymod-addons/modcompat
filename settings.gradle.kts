rootProject.name = "modcompat"

pluginManagement {
    val labyGradlePluginVersion = "0.5.3"
    plugins {
        id("net.labymod.gradle") version (labyGradlePluginVersion)
    }

    buildscript {
        repositories {
            maven("https://dist.labymod.net/api/v1/maven/release/")
            maven("https://maven.neoforged.net/releases/")
            maven("https://maven.fabricmc.net/")
            gradlePluginPortal()
            mavenCentral()
            mavenLocal()
        }

        dependencies {
            classpath("net.labymod.gradle", "common", labyGradlePluginVersion)
        }
    }
}

plugins.apply("net.labymod.labygradle.settings")

include(":api")
include(":core")
include(":mod-compatibility")

labyMod {
    asVersionedModule {
        findProject(":mod-compatibility")?.projectDir?.listFiles()?.forEach {
            if (it.isDirectory && it.name != "src" && it.name != "build") {
                withBaseDirectory(file("mod-compatibility")) {
                    defineModule(it.name)
                }
            }
        }
    }
}

