plugins {
    id("net.fabricmc.fabric-loom") version "1.19.3"
}

fun prop(name: String) = providers.gradleProperty(name).get()

version = prop("mod_version")
group = prop("maven_group")

base {
    archivesName = "EasyArmorStands-Polymer"
}

repositories {
    maven("https://maven.nucleoid.xyz/") { name = "Nucleoid" }
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${prop("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${prop("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${prop("fabric_api_version")}")

    // Polymer: client-side-only "virtual" display entities used to render the editor gizmos
    implementation(include("eu.pb4:polymer-virtual-entity:${prop("polymer_version")}")!!)
    include("eu.pb4:polymer-common:${prop("polymer_version")}")

    // Text (MiniMessage, server-side translations)
    implementation(include("net.kyori:adventure-platform-fabric:${prop("adventure_platform_version")}")!!)

    // Commands
    implementation(include("org.incendo:cloud-fabric:${prop("cloud_modded_version")}")!!)
    implementation(include("org.incendo:cloud-annotations:${prop("cloud_version")}")!!)
    annotationProcessor("org.incendo:cloud-annotations:${prop("cloud_version")}")
    implementation(include("org.incendo:cloud-minecraft-extras:${prop("cloud_minecraft_version")}") {
        exclude(group = "net.kyori")
    })

    // Configuration
    implementation(include("org.spongepowered:configurate-yaml:${prop("configurate_version")}")!!)
    include("org.spongepowered:configurate-core:${prop("configurate_version")}")
    include("io.leangen.geantyref:geantyref:1.3.16")
    include("net.kyori:option:1.1.0")

    // Permissions (LuckPerms etc.)
    implementation(include("me.lucko:fabric-permissions-api:${prop("permissions_api_version")}")!!)
}

loom {
    accessWidenerPath = file("src/main/resources/easyarmorstands.classtweaker")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
    withSourcesJar()
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release = 25
        options.compilerArgs.addAll(listOf("-Xmaxerrs", "2000"))
    }

    processResources {
        val props = mapOf(
            "version" to project.version,
            "minecraft_version" to prop("minecraft_version"),
            "loader_version" to prop("min_loader_version"),
        )
        inputs.properties(props)
        filesMatching("fabric.mod.json") {
            expand(props)
        }
    }

    jar {
        from("LICENSE") {
            rename { "LICENSE_EasyArmorStands-Polymer" }
        }
        from("NOTICE.md")
    }
}

fabricApi {
    configureTests {
        createSourceSet = true
        modId = "easyarmorstands-gametest"
        enableGameTests = true
        enableClientGameTests = false
        eula = true
    }
}
