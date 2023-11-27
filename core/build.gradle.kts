import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.io.FileReader
import java.io.FileWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files

version = "0.1.0"

plugins {
    id("java-library")
}

dependencies {
    api(project(":api"))
    labyApi("core")
}

labyModProcessor {
    referenceType = net.labymod.gradle.core.processor.ReferenceType.DEFAULT
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


tasks.register("buildIndex") {
    doFirst {
        val gson = GsonBuilder().create()
        val index = JsonArray()

        fileTree("${project.parent?.projectDir}/mod-issues/") {
            include("**/manifest.json")
        }.forEach { file ->
            FileReader(file, StandardCharsets.UTF_8).use {
                index.add(JsonParser.parseReader(it))
            }
        }

        val resourcesDir = project.buildDir.resolve("resources/main/")
        Files.createDirectories(resourcesDir.toPath())
        FileWriter(resourcesDir.resolve("index.json")).use { gson.toJson(index, it) }
    }
}

tasks.compileJava {
    dependsOn(tasks["buildIndex"])
}