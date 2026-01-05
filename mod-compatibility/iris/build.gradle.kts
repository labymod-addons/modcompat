import net.labymod.labygradle.common.internal.fabric.dependency.ModrinthDependencyHandler
import org.gradle.kotlin.dsl.findByType
import kotlin.apply

dependencies {
    api(project(":core"))
    compileOnly("maven.modrinth:iris:1.4.5+1.16.5")

    extensions.findByType(ModrinthDependencyHandler::class)?.apply {
        this.modrinth("1.16.5", "iris", "1.4.5+1.16.5")
        this.modrinth("1.17.1", "iris", "1.17.x-v1.2.6")
        this.modrinth("1.18.2", "iris", "1.6.11+1.18.2")
        this.modrinth("1.19.4", "iris", "1.6.11+1.19.4")
        this.modrinth("1.20.1", "iris", "1.7.6+1.20.1")
        this.modrinth("1.20.6", "iris", "1.7.2+1.20.6")
        this.modrinth("1.21", "iris", "1.8.8+1.21.1-fabric")
        this.modrinth("1.21.1", "iris", "1.8.8+1.21.1-fabric")
        this.modrinth("1.21.3", "iris", "1.8.1+1.21.3-fabric")
        this.modrinth("1.21.4", "iris", "1.8.8+1.21.4-fabric")
        this.modrinth("1.21.5", "iris", "1.8.11+1.21.5-fabric")
        this.modrinth("1.21.8", "iris", "1.9.6+1.21.8-fabric")
        this.modrinth("1.21.10", "iris", "1.9.6+1.21.10-fabric")
        this.modrinth("1.21.11", "iris", "1.10.4+1.21.11-fabric")
    }
}