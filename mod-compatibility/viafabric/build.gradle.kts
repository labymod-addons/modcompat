import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType
import net.labymod.labygradle.common.internal.fabric.dependency.ModrinthDependencyHandler

dependencies {
    api(project(":core"))

    modrinth {
        this.modrinth("1.16.5", "viafabric", "0.4.22+184-1.14-1.21")
        this.modrinth("1.17.1", "viafabric", "0.4.22+184-1.14-1.21")
        this.modrinth("1.18.2", "viafabric", "0.4.22+184-1.14-1.21")
        this.modrinth("1.19.4", "viafabric", "0.4.22+184-1.14-1.21")
        this.modrinth("1.20.1", "viafabric", "0.4.18+109-main")
        this.modrinth("1.20.6", "viafabric", "0.4.22+184-1.14-1.21")
        this.modrinth("1.21", "viafabric", "0.4.15+84-main")
        this.modrinth("1.21.1", "viafabric", "0.4.15+84-main")
        this.modrinth("1.21.3", "viafabric", "0.4.16+88-main")
        this.modrinth("1.21.4", "viafabric", "0.4.17+102-main")
        this.modrinth("1.21.5", "viafabric", "0.4.18+109-main")
        this.modrinth("1.21.8", "viafabric", "0.4.19+118-main")
        this.modrinth("1.21.10", "viafabric", "0.4.20+126-main")
        this.modrinth("1.21.11", "viafabric", "0.4.22+184-1.14-1.21")
        this.modrinth("26.1", "viafabric", "0.4.22+188-26.x")
        this.modrinth("26.1.1", "viafabric", "0.4.22+188-26.x")
        this.modrinth("26.1.2", "viafabric", "0.4.22+188-26.x")
        this.modrinth("26.2", "viafabric", "0.4.22+188-26.x")
        this.modrinth("26.3", "viafabric", "0.4.22+188-26.x")
    }
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}
