import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType
import net.labymod.labygradle.common.internal.fabric.dependency.ModrinthDependencyHandler

dependencies {
    api(project(":core"))

    modrinth {
        this.modrinth("1.19.4", "viafabricplus", "2.7.5")
        this.modrinth("1.20.1", "viafabricplus", "2.8.7")
        this.modrinth("1.20.6", "viafabricplus", "3.3.2")
        this.modrinth("1.21", "viafabricplus", "3.4.9")
        this.modrinth("1.21.1", "viafabricplus", "3.4.9")
        this.modrinth("1.21.3", "viafabricplus", "3.5.3")
        this.modrinth("1.21.4", "viafabricplus", "4.0.5-BACKPORT")
        this.modrinth("1.21.5", "viafabricplus", "4.1.5")
        this.modrinth("1.21.8", "viafabricplus", "4.2.5")
        this.modrinth("1.21.10", "viafabricplus", "4.3.6")
        this.modrinth("1.21.11", "viafabricplus", "4.4.15")
        this.modrinth("26.1", "viafabricplus", "4.5.6")
        this.modrinth("26.1.1", "viafabricplus", "4.5.6")
        this.modrinth("26.1.2", "viafabricplus", "4.5.6")
        this.modrinth("26.2", "viafabricplus", "5.0.3")
        this.modrinth("26.3", "viafabricplus", "5.1.2")
    }
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}
