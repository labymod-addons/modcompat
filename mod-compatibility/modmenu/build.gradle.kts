import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType
import net.labymod.labygradle.common.internal.fabric.dependency.ModrinthDependencyHandler

dependencies {
    api(project(":core"))

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
        this.modrinth("1.21.5", "modmenu", "14.0.0-rc.2")
        this.modrinth("1.21.8", "modmenu", "15.0.0")
        this.modrinth("1.21.10", "modmenu", "16.0.0-rc.1")
        this.modrinth("1.21.11", "modmenu", "17.0.0-beta.1")
    }
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}