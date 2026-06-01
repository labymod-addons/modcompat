import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType
import net.labymod.labygradle.common.extension.model.labymod.ReleaseChannel
import net.labymod.labygradle.common.extension.model.labymod.ReleaseChannels
import net.labymod.labygradle.common.internal.gradle.ProjectUtil
import net.labymod.labygradle.common.internal.labymod.addon.model.AddonMeta
import java.net.HttpURLConnection
import java.net.URI

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
            javaVersion = JavaVersion.VERSION_21
            runs {
                getByName("client") {
                    jvmArgs("-Dmixin.debug=false")
                    jvmArgs("-Dmixin.debug.export=true")
                    jvmArgs("-Dmixin.debug.verbose=true")
                    jvmArgs("-Dmixin.env.disableRefMap=false")
                }
            }
        }
    }

    addonInfo {
        namespace = "modcompat"
        displayName = "Mod Compat"
        author = "LabyMod"
        description = "LabyMod mod compatibility for external mods"
        minecraftVersion = "*"
        version = providers.environmentVariable("VERSION").getOrElse(project.version.toString())
        meta(AddonMeta.HIDDEN)
        releaseChannel = ReleaseChannels.SNAPSHOT
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")
    plugins.apply("net.labymod.labygradle.fabric")

    repositories {
        maven("https://api.modrinth.com/maven")
    }

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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

tasks.register("latestModVersions") {
    description = "Prints this.modrinth(\"mc\", \"slug\", \"ver\") lines with the latest version per MC version. Usage: ./gradlew latestModVersions -Pslug=sodium [-Ploader=fabric] -q"
    group = "verification"

    val slugProp = providers.gradleProperty("slug")
    val loaderProp = providers.gradleProperty("loader").orElse("fabric")
    val mcVersionsProp = providers.gradleProperty("net.labymod.minecraft-versions")

    doLast {
        val slug = slugProp.orNull ?: error("Missing -Pslug=<modrinth-slug> (e.g. -Pslug=sodium)")
        val loader = loaderProp.get()
        val mcVersions = mcVersionsProp.get().split(";")

        val url = URI(
            "https://api.modrinth.com/v2/project/$slug/version?loaders=%5B%22$loader%22%5D"
        ).toURL()
        val conn = url.openConnection() as HttpURLConnection
        conn.setRequestProperty("User-Agent", "labymod/modcompat (gradle:latestModVersions)")

        @Suppress("UNCHECKED_CAST")
        val versions = conn.inputStream.use {
            groovy.json.JsonSlurper().parse(it.reader())
        } as List<Map<String, Any>>

        for (mc in mcVersions) {
            @Suppress("UNCHECKED_CAST")
            val latest = versions.firstOrNull { (it["game_versions"] as List<String>).contains(mc) }
            if (latest == null) {
                println("// no $loader version of $slug for $mc")
            } else {
                println("this.modrinth(\"$mc\", \"$slug\", \"${latest["version_number"]}\")")
            }
        }
    }
}