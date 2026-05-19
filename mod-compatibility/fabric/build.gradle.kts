import net.labymod.labygradle.common.extension.model.GameVersion
import net.labymod.labygradle.common.internal.fabric.dependency.ModrinthDependencyHandler
import net.labymod.labygradle.common.internal.gradle.ProjectUtil
import net.labymod.labygradle.common.internal.labymod.LabyModPlugin
import net.neoforged.srgutils.IMappingBuilder
import net.neoforged.srgutils.IMappingFile
import org.gradle.kotlin.dsl.findByType
import java.io.FileNotFoundException
import java.nio.file.Files
import kotlin.apply

val accessWatchers = arrayOf(
        "net/minecraft/world/entity/Display\$TextDisplay",
        "net/minecraft/world/entity/Display\$ItemDisplay",
        "net/minecraft/world/entity/Display\$BlockDisplay"
)

repositories {
    maven("https://maven.fabricmc.net/") {
        name = "FabricMC"
    }
}

dependencies {
    compileOnly("net.fabricmc:fabric-loader:0.16.10")

    extensions.findByType(ModrinthDependencyHandler::class)?.apply {
        this.modrinth("1.16.5", "fabric-api", "0.42.0+1.16")
        this.modrinth("1.17.1", "fabric-api", "0.46.1+1.17")
        this.modrinth("1.18.2", "fabric-api", "0.77.0+1.18.2")
        this.modrinth("1.19.4", "fabric-api", "0.87.2+1.19.4")
        this.modrinth("1.20.1", "fabric-api", "0.92.9+1.20.1")
        this.modrinth("1.20.6", "fabric-api", "0.100.8+1.20.6")
        this.modrinth("1.21", "fabric-api", "0.102.0+1.21")
        this.modrinth("1.21.1", "fabric-api", "0.116.12+1.21.1")
        this.modrinth("1.21.3", "fabric-api", "0.114.1+1.21.3")
        this.modrinth("1.21.4", "fabric-api", "0.119.4+1.21.4")
        this.modrinth("1.21.5", "fabric-api", "0.128.2+1.21.5")
        this.modrinth("1.21.8", "fabric-api", "0.136.1+1.21.8")
        this.modrinth("1.21.10", "fabric-api", "0.138.4+1.21.10")
        this.modrinth("1.21.11", "fabric-api", "0.141.4+1.21.11")
    }
}

tasks.create("generateAccessWatcher") {
    doLast {
        val mappingsDirectory = plugins.getPlugin(LabyModPlugin::class).files.mappingsDirectory
        for (sourceSet in sourceSets) {
            val extra = sourceSet.extra
            if (!extra.has("lg_game_version")) {
                continue
            }

            val version = extra["lg_game_version"] as GameVersion
            val resolveDependencyPath = ProjectUtil.resolveDependencyPath(mappingsDirectory, version.mappings.dependencies.first())

            if (!Files.exists(resolveDependencyPath)) {
                throw FileNotFoundException("$resolveDependencyPath could not be found")
            }

            val mappings = IMappingFile.load(resolveDependencyPath.toFile())
            val reversedMappings = mappings.reverse()

            val builder = IMappingBuilder.create("left", "right")

            accessWatchers.forEach { watcher ->
                val selectedClass = reversedMappings.getClass(watcher) ?: return@forEach
                mappings.getClass(selectedClass.mapped)?.apply {
                    val newClass = builder.addClass(original, mapped)
                    methods.forEach { method ->
                        newClass.method(method.descriptor, method.original, method.mapped)
                    }
                }
            }

            val build = builder.build()
            val newMappings = build.getMap("left", "right")

            if (!newMappings.classes.isEmpty()) {
                val destination = project.layout.projectDirectory.asFile.toPath()
                        .resolve("src")
                        .resolve(version.sourceSetName)
                        .resolve("resources")
                        .resolve("access_watchers")
                        .resolve("${project.name}-${version.versionId}.tsrg2")
                newMappings.write(destination, IMappingFile.Format.TSRG2, false)
            }
        }
    }
}
