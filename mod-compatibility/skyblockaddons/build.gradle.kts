import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    api(project(":core"))

    v1_8_9CompileOnly(files("./libs/skyblockaddons-1.8.9.jar"))
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}