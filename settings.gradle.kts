rootProject.name = "modcompat"

pluginManagement {
    repositories {
        maven("https://maven.laby.net/api/v1/maven/release/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        id("net.labymod.labygradle.settings") version "0.8.1"
    }
}

plugins {
    id("net.labymod.labygradle.settings")
}

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

