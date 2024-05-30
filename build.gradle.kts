import net.labymod.gradle.core.addon.info.AddonMeta
import net.labymod.gradle.core.dsl.getClientRepository

plugins {
    id("java-library")
    id("net.labymod.gradle")
    id("net.labymod.gradle.addon")
}

group = "net.labymod.addons"
version = "0.0.1"

labyMod {
    defaultPackageName = "net.labymod.addons.modcompat"
    addonInfo {
        namespace = "modcompat"
        displayName = "Mod Compat"
        author = "LabyMod"
        description = "LabyMod mod compatibility for external mods"
        minecraftVersion = "*"
        version = System.getenv().getOrDefault("VERSION", project.version.toString())
        meta(AddonMeta.HIDDEN)
    }

    minecraft {
        registerVersions(
                "1.8.9",
                "1.12.2",
                "1.16.5",
                "1.17.1",
                "1.18.2",
                "1.19.2",
                "1.19.3",
                "1.19.4",
                "1.20.1",
                "1.20.2",
                "1.20.4",
                "1.20.5",
                "1.20.6",
        ) { version, provider ->
            configureRun(provider, version)
        }

        subprojects.forEach {
            if (it.name != "game-runner" && it.parent?.name != "mod-compatibility") {
                filter(it.name)
            }
        }
    }

    addonDev {
        productionRelease()
    }
}

allprojects {
    repositories {
        mavenLocal()
    }
}

subprojects {
    plugins.apply("java-library")
    plugins.apply("net.labymod.gradle")
    plugins.apply("net.labymod.gradle.addon")

    repositories {
        maven("https://libraries.minecraft.net/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
}

fun configureRun(provider: net.labymod.gradle.core.minecraft.provider.VersionProvider, gameVersion: String) {
    provider.runConfiguration {
        mainClass = "net.minecraft.launchwrapper.Launch"
        jvmArgs("-Dnet.labymod.running-version=${gameVersion}")
        // jvmArgs("-Dmixin.debug=true")
        jvmArgs("-Dmixin.debug.export=true")
        jvmArgs("-Dmixin.debug.verbose=true")
        jvmArgs("-Dnet.labymod.debugging.all=true")
        jvmArgs("-Dnet.labymod.debugging.asm=true")

        val obfuscatedClientJar = getClientRepository(gameVersion).resolve("client-$gameVersion-obfuscated.jar")
        jvmArgs("-Doptifabric.mc-jar=${obfuscatedClientJar.toAbsolutePath()}")
        jvmArgs("-Doptifabric.extract=true")

        jvmArgs("-Dnet.minecraftforge.gradle.GradleStart.srg.srg-mcp=${project.getClientRepository(gameVersion).resolve("client-${gameVersion}-mappings.txt")}")

        args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.LabyModLaunchWrapperTweaker")
        args("--labymod-dev-environment", "true")
        args("--addon-dev-environment", "true")
    }

    provider.javaVersion = JavaVersion.VERSION_21

    provider.mixin {
        val mixinMinVersion = when (gameVersion) {
            "1.8.9", "1.12.2", "1.16.5" -> {
                "0.6.6"
            }

            else -> {
                "0.8.2"
            }
        }

        minVersion = mixinMinVersion
    }
}