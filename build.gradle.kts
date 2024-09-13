import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType
import net.labymod.labygradle.common.internal.gradle.ProjectUtil
import net.labymod.labygradle.common.internal.labymod.addon.model.AddonMeta

group = "net.labymod.addons"
version = "0.0.1"

plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

labyMod {
    defaultPackageName = "net.labymod.addons.modcompat"

    minecraft {
        registerVersion(versions.toTypedArray()) {
        }
    }

    addonInfo {
        namespace = "modcompat"
        displayName = "Mod Compat"
        author = "LabyMod"
        description = "LabyMod mod compatibility for external mods"
        minecraftVersion = "*"
        version = System.getenv().getOrDefault("VERSION", project.version.toString())
        meta(AddonMeta.HIDDEN)
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    repositories {
        maven("https://api.modrinth.com/maven")
    }

    if (ProjectUtil.isVersionedModule(this)) {
        dependencies {
            labyProcessor()
            labyApi("processor")
            api(project(":core"))
        }

        if (name != "game-runner") {
            labyModAnnotationProcessor {
                referenceType = ReferenceType.DEFAULT
            }
        }
    }
}