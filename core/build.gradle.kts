import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.io.FileReader
import java.io.FileWriter
import java.nio.charset.StandardCharsets

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

        fileTree("${project.projectDir}/mod-issues/") {
            include("**/manifest.json")
        }.forEach { file ->
            FileReader(file, StandardCharsets.UTF_8).use {
                index.add(JsonParser.parseReader(it))
            }
        }

        FileWriter(project.buildDir.resolve("resources/main/index.json"))
                .use { gson.toJson(index, it) }
    }
}

tasks.compileJava {
    dependsOn(tasks["buildIndex"])
}