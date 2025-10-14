import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType
import net.labymod.labygradle.common.internal.fabric.dependency.ModrinthDependencyHandler

dependencies {
    api(project(":core"))

    for (sourceSet in sourceSets) {
        if (sourceSet.name == "v1_8_9") {
            add(sourceSet.compileOnlyConfigurationName, "maven.modrinth:replaymod:1.8.9-2.6.20")
        } else {
            add(sourceSet.compileOnlyConfigurationName, "maven.modrinth:replaymod:1.20.4-2.6.15")
        }
    }
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}