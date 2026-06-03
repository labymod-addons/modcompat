import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType
import net.labymod.labygradle.common.internal.fabric.dependency.ModrinthDependencyHandler

dependencies {
    api(project(":core"))

    for (sourceSet in sourceSets) {
        when (sourceSet.name) {
            "v1_8_9" -> {
                add(sourceSet.compileOnlyConfigurationName, "maven.modrinth:replaymod:1.8.9-2.6.20")
            }
            "v1_12_2" -> {
                add(sourceSet.compileOnlyConfigurationName, "maven.modrinth:replaymod:1.12.2-2.6.20")
            }
            "v26_1" -> {
                add(sourceSet.compileOnlyConfigurationName, "maven.modrinth:replaymod:26.1-2.6.26")
            }
            "v26_1_1" -> {
                add(sourceSet.compileOnlyConfigurationName, "maven.modrinth:replaymod:26.1.1-2.6.26")
            }
            "v26_1_2" -> {
                add(sourceSet.compileOnlyConfigurationName, "maven.modrinth:replaymod:26.1.2-2.6.26")
            }
        }
    }

    modrinth {
        this.modrinth("1.16.5", "replaymod", "1.16.4-2.6.20")
        this.modrinth("1.17.1", "replaymod", "1.17.1-2.6.20")
        this.modrinth("1.18.2", "replaymod", "1.18.2-2.6.20")
        this.modrinth("1.19.4", "replaymod", "1.19.4-2.6.20")
        this.modrinth("1.20.1", "replaymod", "1.20.1-2.6.23")
        this.modrinth("1.20.6", "replaymod", "1.20.6-2.6.23")
        this.modrinth("1.21", "replaymod", "1.21-2.6.23")
        this.modrinth("1.21.1", "replaymod", "1.21-2.6.23")
        this.modrinth("1.21.3", "replaymod", "1.21.2-2.6.23")
        this.modrinth("1.21.4", "replaymod", "1.21.4-2.6.23")
        this.modrinth("1.21.5", "replaymod", "1.21.5-2.6.23")
        this.modrinth("1.21.8", "replaymod", "1.21.7-2.6.23")
        this.modrinth("1.21.10", "replaymod", "1.21.10-2.6.25")
        this.modrinth("1.21.11", "replaymod", "1.21.11-2.6.26")
    }
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}