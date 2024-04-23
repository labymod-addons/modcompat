version = "0.1.0"

plugins {
    id("java-library")
}

repositories {
    maven("https://api.modrinth.com/maven")
}

dependencies {
    api(project(":core"))
    compileOnly("maven.modrinth:replaymod:1.20.4-2.6.15")
}

labyModProcessor {
    referenceType = net.labymod.gradle.core.processor.ReferenceType.DEFAULT
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}