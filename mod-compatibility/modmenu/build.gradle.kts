import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType
import net.labymod.labygradle.common.internal.fabric.dependency.ModrinthDependencyHandler

//import net.labymod.labygradle.common.internal.fabric.dependency.ModrinthDependencyHandler

dependencies {
    api(project(":core"))

    // TODO: instead of files(), use remap() and fetch from the modrinth repo
    //v1_16_5CompileOnly(files("./libs/modmenu-1.16.5.jar"))
    //v1_17_1CompileOnly(files("./libs/modmenu-1.17.1.jar"))
    //v1_18_2CompileOnly(files("./libs/modmenu-1.18.2.jar"))
    //v1_19_2CompileOnly(files("./libs/modmenu-1.19.3.jar"))
    //v1_19_3CompileOnly(files("./libs/modmenu-1.19.3.jar"))
    //v1_19_4CompileOnly(files("./libs/modmenu-1.19.4.jar"))
    //v1_20_1CompileOnly(files("./libs/modmenu-1.20.2.jar"))
    //v1_20_2CompileOnly(files("./libs/modmenu-1.20.2.jar"))
    //v1_20_4CompileOnly(files("./libs/modmenu-1.20.4.jar"))
    //v1_20_5CompileOnly(files("./libs/modmenu-1.20.6.jar"))
    //v1_20_6CompileOnly(files("./libs/modmenu-1.20.6.jar"))
    //v1_21CompileOnly(files("./libs/modmenu-1.21.jar"))
    //v1_21_1CompileOnly(files("./libs/modmenu-1.21.1.jar"))

    extensions.findByType(ModrinthDependencyHandler::class)?.apply {
        this.modrinth("1.16.5", "modmenu", "1.16.23")
        this.modrinth("1.17.1", "modmenu", "2.0.17")
        this.modrinth("1.18.2", "modmenu", "3.2.5")
        this.modrinth("1.19.2", "modmenu", "4.2.0-beta.2")
        this.modrinth("1.19.3", "modmenu", "5.1.0")
        this.modrinth("1.19.4", "modmenu", "6.3.1")
        this.modrinth("1.20.1", "modmenu", "7.2.2")
        this.modrinth("1.20.2", "modmenu", "8.0.1")
        this.modrinth("1.20.4", "modmenu", "9.2.0")
        this.modrinth("1.20.5", "modmenu", "10.0.0")
        this.modrinth("1.20.6", "modmenu", "10.0.0")
        this.modrinth("1.21", "modmenu", "11.0.3")
        this.modrinth("1.21.1", "modmenu", "11.0.3")
        this.modrinth("1.21.3", "modmenu", "12.0.0")
        this.modrinth("1.21.4", "modmenu", "13.0.1")
    }

    /*
    extensions.findByType(ModrinthDependencyHandler::class)?.apply {
        println(this)
        v1_21_1CompileOnly(this.modrinth("modmenu", "11.0.2"))
    }*/
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}