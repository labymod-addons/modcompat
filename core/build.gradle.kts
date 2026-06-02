import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import java.io.FileReader
import java.io.FileWriter
import java.net.HttpURLConnection
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    labyProcessor()
    api(project(":api"))
    labyApi("core")
    labyApi("loader-vanilla-launchwrapper")
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}

tasks.register("buildIndex") {
    doLast {
        val gson = GsonBuilder().create()
        val index = JsonArray()

        fileTree("${rootProject.projectDir}/mod-compatibility/") {
            include("**/manifest.json")
        }.forEach { file ->
            FileReader(file, StandardCharsets.UTF_8).use {
                index.add(JsonParser.parseReader(it))
            }
        }

        val resourcesDir = project.layout.buildDirectory.dir("resources/main/").get().asFile
        Files.createDirectories(resourcesDir.toPath())
        FileWriter(resourcesDir.resolve("index.json")).use { gson.toJson(index, it) }
    }
}

tasks.processResources {
    finalizedBy(tasks["buildIndex"])
}

tasks.register("buildCompatibility") {
    description = "Aggregates mod-compatibility manifests into a categorized compatibility.json"
    group = "build"

    val manifests = fileTree("${rootProject.projectDir}/mod-compatibility/") {
        include("**/manifest.json")
    }
    val outputFile = file("${rootProject.projectDir}/mod-compatibility/compatibility.json")

    inputs.files(manifests)
    outputs.file(outputFile)

    doLast {
        val loaders = listOf("fabric", "forge")
        val bucketKeys = listOf("recommended", "incompatible", "replacements")

        fun mapLoader(raw: String): String = when (raw.lowercase()) {
            "fabricloader", "fabric" -> "fabric"
            "forge", "neoforge", "neoforgeloader" -> "forge"
            else -> error("Unknown mod loader '$raw'")
        }

        // bucket -> loader -> (identifier -> entry), kept sorted by identifier for stable output
        val buckets = bucketKeys.associateWith {
            loaders.associateWith { sortedMapOf<String, JsonElement>() }
        }

        manifests.forEach { file ->
            val manifest = FileReader(file, StandardCharsets.UTF_8).use {
                JsonParser.parseReader(it).asJsonObject
            }
            val mod = file.parentFile.name

            val ids = manifest.getAsJsonArray("ids")
            if (ids == null || ids.isEmpty) {
                error("Manifest for '$mod' is missing 'ids'")
            }
            val identifier = ids[0].asString

            val compatibility = if (manifest.has("compatibility")) {
                manifest.getAsJsonObject("compatibility")
            } else {
                null
            }

            val issues = manifest.getAsJsonArray("issues") ?: JsonArray()
            val blocking = issues.any {
                val issue = it.asJsonObject
                val playable = issue.get("playable")?.asBoolean ?: true
                val fixed = issue.get("fixed")?.asBoolean ?: false
                !playable && !fixed
            }

            val category = compatibility?.get("category")?.asString
                ?: if (blocking) "incompatible" else return@forEach
            val bucketKey = when (category) {
                "replacement", "replacements" -> "replacements"
                "recommended", "incompatible" -> category
                else -> error("Unknown compatibility category '$category' for '$mod'")
            }

            val entryLoaders = if (compatibility?.has("loaders") == true) {
                compatibility.getAsJsonArray("loaders").map { it.asString }
            } else {
                (manifest.getAsJsonArray("modLoaders") ?: JsonArray()).map { mapLoader(it.asString) }
            }.distinct()

            val namespace = compatibility?.get("namespace")?.asString
            val versions = compatibility?.get("versions")?.asString

            val entry: JsonElement = when {
                bucketKey == "replacements" -> JsonObject().apply {
                    addProperty("identifier", identifier)
                    addProperty(
                        "namespace",
                        namespace ?: error("Replacement '$mod' requires compatibility.namespace")
                    )
                    if (versions != null) addProperty("versions", versions)
                }

                versions != null -> JsonObject().apply {
                    addProperty("identifier", identifier)
                    addProperty("versions", versions)
                }

                else -> JsonPrimitive(identifier)
            }

            entryLoaders.forEach { loader ->
                buckets.getValue(bucketKey).getValue(loader)[identifier] = entry
            }
        }

        val result = JsonObject()
        for (bucketKey in bucketKeys) {
            val loaderObject = JsonObject()
            for (loader in loaders) {
                val array = JsonArray()
                buckets.getValue(bucketKey).getValue(loader).values.forEach { array.add(it) }
                loaderObject.add(loader, array)
            }
            result.add(bucketKey, loaderObject)
        }

        val gson = GsonBuilder().setPrettyPrinting().create()
        FileWriter(outputFile, StandardCharsets.UTF_8).use { gson.toJson(result, it) }
        println("Wrote ${outputFile.relativeTo(rootProject.projectDir)}")
    }
}

tasks.register("uploadCompatibility") {
    description = "Uploads the generated compatibility.json to the configured endpoint"
    group = "publishing"

    dependsOn("buildCompatibility")

    val compatibilityFile = file("${rootProject.projectDir}/mod-compatibility/compatibility.json")
    val endpointProvider = providers.environmentVariable("MODCOMPAT_UPLOAD_ENDPOINT")
    val apiKeyProvider = providers.environmentVariable("MODCOMPAT_UPLOAD_API_KEY")

    doLast {
        val endpoint = endpointProvider.orNull
            ?: error("Missing MODCOMPAT_UPLOAD_ENDPOINT environment variable")
        val apiKey = apiKeyProvider.orNull
            ?: error("Missing MODCOMPAT_UPLOAD_API_KEY environment variable")
        if (!compatibilityFile.exists()) {
            error("compatibility.json not found at ${compatibilityFile.path}")
        }

        val payload = compatibilityFile.readBytes()
        val connection = URI(endpoint).toURL().openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        connection.setRequestProperty("X-API-Key", apiKey)
        connection.outputStream.use { it.write(payload) }

        val status = connection.responseCode
        val responseStream = if (status in 200..299) connection.inputStream else connection.errorStream
        val body = responseStream?.use { it.reader(StandardCharsets.UTF_8).readText() }.orEmpty()
        if (status !in 200..299) {
            error("Upload failed: HTTP $status${if (body.isBlank()) "" else " - $body"}")
        }

        println("Uploaded compatibility.json to $endpoint (HTTP $status)")
    }
}