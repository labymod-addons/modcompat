import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    api(project(":core"))
    compileOnly("maven.modrinth:replaymod:1.20.4-2.6.15")
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}