
repositories {
    maven("https://api.modrinth.com/maven")
}

dependencies {
    api(project(":core"))
    compileOnly("maven.modrinth:iris:1.4.5+1.16.5")
}