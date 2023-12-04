rootProject.name = "modcompat"

pluginManagement {
    val labyGradlePluginVersion = "0.3.37"
    plugins {
        id("net.labymod.gradle") version (labyGradlePluginVersion)
    }

    buildscript {
        repositories {
            maven("https://dist.labymod.net/api/v1/maven/release/")
            maven("https://repo.spongepowered.org/repository/maven-public")
            mavenCentral()
        }

        dependencies {
            classpath("net.labymod.gradle", "addon", labyGradlePluginVersion)
        }
    }
}

plugins.apply("net.labymod.gradle")

include(":api")
include(":core")
include(":mod-issues")

findProject(":mod-issues")?.projectDir?.listFiles()?.forEach {
    if (it.isDirectory && it.name != "src") {
        include(":mod-issues:${it.name}")
    }
}
