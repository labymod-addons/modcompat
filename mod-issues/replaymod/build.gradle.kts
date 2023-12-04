version = "0.1.0"

plugins {
    id("java-library")
}

repositories {
    maven("https://api.modrinth.com/maven")
}

dependencies {
    api(project(":core"))
    compileOnly("maven.modrinth:replaymod:1.20.1-2.6.13")
}

labyModProcessor {
    referenceType = net.labymod.gradle.core.processor.ReferenceType.DEFAULT
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}