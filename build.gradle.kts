import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType
import net.labymod.labygradle.common.extension.model.labymod.ReleaseChannel
import net.labymod.labygradle.common.extension.model.labymod.ReleaseChannels
import net.labymod.labygradle.common.internal.gradle.ProjectUtil
import net.labymod.labygradle.common.internal.labymod.addon.model.AddonMeta
import java.io.File
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

tasks.register("addMinecraftVersion") {
    description = "Adds a new Minecraft version: appends it to gradle.properties, inserts a modrinth(...) line into every mod-compatibility build file, and clones each module's previous versioned source set. Usage: ./gradlew addMinecraftVersion -Pmc=26.2 [-Pfrom=26.1.2] [-Ploader=fabric] -q"
    group = "build setup"

    val mcProp = providers.gradleProperty("mc")
    val fromProp = providers.gradleProperty("from")
    val loaderProp = providers.gradleProperty("loader").orElse("fabric")
    val propsFile = rootProject.file("gradle.properties")
    val modCompatDir = rootProject.file("mod-compatibility")

    doLast {
        val mc = mcProp.orNull ?: error("Missing -Pmc=<new-version> (e.g. -Pmc=26.2)")
        val loader = loaderProp.get()
        val newToken = "v" + mc.replace(".", "_")

        val key0 = "net.labymod.minecraft-versions="
        val currentVersions = propsFile.readLines()
            .first { it.startsWith(key0) }
            .substring(key0.length)
            .split(";")
        val previous = fromProp.orNull
            ?: currentVersions.lastOrNull { it != mc }
            ?: error("No previous version found to clone from")
        val fromToken = "v" + previous.replace(".", "_")

        fun rewrite(file: File, transform: (MutableList<String>) -> Boolean) {
            val raw = file.readText()
            val sep = if (raw.contains("\r\n")) "\r\n" else "\n"
            val hadTrailingNewline = raw.endsWith("\n")
            val lines = file.readLines().toMutableList()
            if (!transform(lines)) return
            file.writeText(lines.joinToString(sep) + if (hadTrailingNewline) sep else "")
        }

        fun latestVersion(slug: String): String? {
            val url = URI(
                "https://api.modrinth.com/v2/project/$slug/version?loaders=%5B%22$loader%22%5D"
            ).toURL()
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("User-Agent", "labymod/modcompat (gradle:addMinecraftVersion)")
            @Suppress("UNCHECKED_CAST")
            val versions = conn.inputStream.use {
                groovy.json.JsonSlurper().parse(it.reader())
            } as List<Map<String, Any>>
            @Suppress("UNCHECKED_CAST")
            val latest = versions.firstOrNull { (it["game_versions"] as List<String>).contains(mc) }
            return latest?.get("version_number") as String?
        }

        val key = "net.labymod.minecraft-versions="
        rewrite(propsFile) { lines ->
            val idx = lines.indexOfFirst { it.startsWith(key) }
            if (idx < 0) error("Could not find '$key' in $propsFile")
            val current = lines[idx].substring(key.length).split(";")
            if (current.contains(mc)) {
                println("gradle.properties already lists $mc, leaving it untouched")
                false
            } else {
                lines[idx] = lines[idx] + ";" + mc
                println("gradle.properties: added $mc")
                true
            }
        }

        val lineRegex = Regex("""^(\s*)this\.modrinth\("([^"]+)",\s*"([^"]+)",\s*"([^"]+)"\)\s*$""")
        val buildFiles = (modCompatDir.listFiles() ?: emptyArray())
            .filter { it.isDirectory }
            .map { File(it, "build.gradle.kts") }
            .filter { it.exists() && it.readText().contains("this.modrinth(") }
            .sortedBy { it.parentFile.name }

        val depMissing = mutableSetOf<String>()
        for (file in buildFiles) {
            val name = file.parentFile.name
            rewrite(file) { lines ->
                var lastIdx = -1
                var indent = ""
                var slug: String? = null
                var alreadyHas = false
                for ((i, line) in lines.withIndex()) {
                    val m = lineRegex.find(line) ?: continue
                    lastIdx = i
                    indent = m.groupValues[1]
                    slug = m.groupValues[3]
                    if (m.groupValues[2] == mc) alreadyHas = true
                }
                if (lastIdx < 0 || slug == null) {
                    println("$name: no modrinth line found, skipping")
                    return@rewrite false
                }
                val todoText = "// no $loader version of $slug for $mc"
                if (lines.any { it.trim() == todoText }) {
                    depMissing.add(name)
                    println("$name: already flagged (no $loader version for $mc), leaving it untouched")
                    return@rewrite false
                }
                if (alreadyHas) {
                    println("$name: already has a line for $mc, leaving it untouched")
                    return@rewrite false
                }
                val resolved = latestVersion(slug!!)
                val newLine = if (resolved != null) {
                    println("$name: + this.modrinth(\"$mc\", \"$slug\", \"$resolved\")")
                    "${indent}this.modrinth(\"$mc\", \"$slug\", \"$resolved\")"
                } else {
                    depMissing.add(name)
                    println("$name: no $loader version of $slug for $mc, inserting TODO comment")
                    "$indent// no $loader version of $slug for $mc"
                }
                lines.add(lastIdx + 1, newLine)
                true
            }
        }

        println()
        println("Cloning '$fromToken' source sets -> '$newToken' (from $previous)")
        for (moduleDir in (modCompatDir.listFiles() ?: emptyArray()).filter { it.isDirectory }.sortedBy { it.name }) {
            val name = moduleDir.name
            val fromRoot = File(moduleDir, "src/$fromToken")
            val sources = if (fromRoot.isDirectory) {
                fromRoot.walkTopDown().filter { it.isFile && it.extension == "java" }.toList()
            } else {
                emptyList()
            }
            if (sources.isEmpty()) continue
            if (name in depMissing) {
                println("$name: has $fromToken source but no $loader dependency for $mc yet, skipping clone")
                continue
            }
            var copied = 0
            var skipped = 0
            for (src in sources) {
                val target = File(src.path.replace(fromToken, newToken))
                if (target.exists()) {
                    skipped++
                    continue
                }
                target.parentFile.mkdirs()
                target.writeText(src.readText().replace(fromToken, newToken))
                copied++
            }
            println("$name: cloned $copied file(s)" + if (skipped > 0) ", $skipped already present" else "")
        }

        println()
        println("Done. Review the diff, build the new version, then fix any version-gated Java by hand.")
    }
}