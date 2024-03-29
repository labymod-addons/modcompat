version = "0.1.0"

plugins {
    id("java-library")
}

repositories {
}

dependencies {
    api(project(":core"))

    // TODO: instead of files(), use remap() and fetch from the modrinth repo
    v1_19_2CompileOnly(files("./libs/modmenu-1.19.3.jar"))
    v1_19_3CompileOnly(files("./libs/modmenu-1.19.3.jar"))
    v1_19_4CompileOnly(files("./libs/modmenu-1.19.4.jar"))
    v1_20_1CompileOnly(files("./libs/modmenu-1.20.2.jar"))
    v1_20_2CompileOnly(files("./libs/modmenu-1.20.2.jar"))
    v1_20_4CompileOnly(files("./libs/modmenu-1.20.4.jar"))
}

labyModProcessor {
    referenceType = net.labymod.gradle.core.processor.ReferenceType.DEFAULT
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}