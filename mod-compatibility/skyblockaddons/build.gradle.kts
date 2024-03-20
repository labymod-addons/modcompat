version = "0.1.0"

plugins {
    id("java-library")
}

repositories {
}

dependencies {
    api(project(":core"))

    v1_8_9CompileOnly(files("./libs/skyblockaddons-1.8.9.jar"))
}

labyModProcessor {
    referenceType = net.labymod.gradle.core.processor.ReferenceType.DEFAULT
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}