import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    api(project(":core"))

    // TODO: instead of files(), use remap() and fetch from the modrinth repo
    v1_16_5CompileOnly(files("./libs/modmenu-1.16.5.jar"))
    v1_17_1CompileOnly(files("./libs/modmenu-1.17.1.jar"))
    v1_18_2CompileOnly(files("./libs/modmenu-1.18.2.jar"))
    v1_19_2CompileOnly(files("./libs/modmenu-1.19.3.jar"))
    v1_19_3CompileOnly(files("./libs/modmenu-1.19.3.jar"))
    v1_19_4CompileOnly(files("./libs/modmenu-1.19.4.jar"))
    v1_20_1CompileOnly(files("./libs/modmenu-1.20.2.jar"))
    v1_20_2CompileOnly(files("./libs/modmenu-1.20.2.jar"))
    v1_20_4CompileOnly(files("./libs/modmenu-1.20.4.jar"))
    v1_20_5CompileOnly(files("./libs/modmenu-1.20.6.jar"))
    v1_20_6CompileOnly(files("./libs/modmenu-1.20.6.jar"))
    v1_21CompileOnly(files("./libs/modmenu-1.21.jar"))
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}