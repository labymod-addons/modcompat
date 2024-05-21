subprojects {
    version = "0.1.0"

    apply(plugin = "java-library")

    dependencies {
        api(project(":core"))
    }

    labyModProcessor {
        referenceType = net.labymod.gradle.core.processor.ReferenceType.DEFAULT
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}