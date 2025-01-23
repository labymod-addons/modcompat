import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.io.FileReader
import java.io.FileWriter
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