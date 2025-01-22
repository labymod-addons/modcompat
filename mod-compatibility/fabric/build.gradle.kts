import net.labymod.labygradle.common.extension.model.GameVersion
import net.labymod.labygradle.common.internal.gradle.ProjectUtil
import net.labymod.labygradle.common.internal.labymod.LabyModPlugin
import net.neoforged.srgutils.IMappingBuilder
import net.neoforged.srgutils.IMappingFile
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

val accessWidenerWatchers = arrayOf(
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
}

tasks.findByName("build")?.apply {
    dependsOn("generateMappings")
}

tasks.create("generateMappings") {
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

            accessWidenerWatchers.forEach { watcher ->
                val selectedClass = reversedMappings.getClass(watcher) ?: return@forEach
                val clazz = mappings.getClass(selectedClass.mapped)
                if (clazz == null) {
                    println("Not found")
                } else {

                    val newClass = builder.addClass(clazz.original, clazz.mapped)
                    clazz.methods.forEach {
                        newClass.method(it.descriptor, it.original, it.mapped)
                    }
                }
            }

            val build = builder.build()
            val newMappings = build.getMap("left", "right")

            if (!newMappings.classes.isEmpty()) {
                val destination = project.layout.buildDirectory.get().asFile.toPath()
                        .resolve("resources")
                        .resolve(version.sourceSetName)
                        .resolve("${project.name}-${version.versionId}.tsrg2")
                newMappings.write(destination, IMappingFile.Format.TSRG2, false)
                println(destination.toAbsolutePath())
            }
        }
    }
}
