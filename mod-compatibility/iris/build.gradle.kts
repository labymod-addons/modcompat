version = "0.1.0"

plugins {
    id("java-library")
}

repositories {
    maven("https://api.modrinth.com/maven")
}

dependencies {
    api(project(":core"))
    compileOnly("maven.modrinth:iris:1.4.5+1.16.5")
}

labyModProcessor {
    referenceType = net.labymod.gradle.core.processor.ReferenceType.DEFAULT
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}