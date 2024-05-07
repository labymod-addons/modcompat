rootProject.name = "modcompat"

pluginManagement {
    val labyGradlePluginVersion = "0.3.49"
    plugins {
        id("net.labymod.gradle") version (labyGradlePluginVersion)
    }

    buildscript {
        repositories {
            maven("https://dist.labymod.net/api/v1/maven/release/")
            maven("https://repo.spongepowered.org/repository/maven-public")
            mavenCentral()
            mavenLocal()
        }

        dependencies {
            classpath("net.labymod.gradle", "addon", labyGradlePluginVersion)
        }
    }
}

plugins.apply("net.labymod.gradle")

include(":api")
include(":core")
include(":mod-compatibility")

findProject(":mod-compatibility")?.projectDir?.listFiles()?.forEach {
    if (it.isDirectory && it.name != "src") {
        include(":mod-compatibility:${it.name}")
    }
}
