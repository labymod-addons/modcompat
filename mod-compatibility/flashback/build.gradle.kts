import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType
import net.labymod.labygradle.common.internal.fabric.dependency.ModrinthDependencyHandler

dependencies {
    api(project(":core"))

    modrinth {
        this.modrinth("1.21", "flashback", "0.39.5")
        this.modrinth("1.21.1", "flashback", "0.39.5")
        // no fabric version of flashback for 1.21.3
        this.modrinth("1.21.4", "flashback", "0.39.5")
        this.modrinth("1.21.5", "flashback", "0.39.5")
        this.modrinth("1.21.8", "flashback", "0.39.5")
        this.modrinth("1.21.10", "flashback", "0.39.5")
        this.modrinth("1.21.11", "flashback", "0.39.5")
        this.modrinth("26.1", "flashback", "0.40.0")
        this.modrinth("26.1.1", "flashback", "0.40.0")
        this.modrinth("26.1.2", "flashback", "0.40.0")
        this.modrinth("26.2", "flashback", "0.41.1")
        this.modrinth("26.3", "flashback", "0.43.6")
    }
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}
