plugins {
    id("fabric-loom") version "1.3.9"
    kotlin("jvm") version "1.9.0"
    id("maven-publish")
}
base.archivesName.set(project.properties["archives_base_name"] as String)
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String


repositories {
    maven { url = uri("https://minecraft.curseforge.com/api/maven") }
    maven { url = uri("https://maven.shedaniel.me/") }
    maven { url = uri("https://maven.terraformersmc.com/") }
    maven { url = uri("https://maven.teamvoided.org/releases") }
    maven { url = uri ("https://maven.quiltmc.org/repository/release") }

}

dependencies {

    minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")
    mappings ("org.quiltmc:quilt-mappings:${project.properties["minecraft_version"]}+build.${project.properties["quilt_mappings"]}:intermediary-v2")
//    mappings("net.fabricmc:yarn:${project.properties["yarn_mappings"]}:v2")

    modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_version"]}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.properties["fabric_kotlin_version"]}")

    //TwitchAPI
    implementation("com.github.twitch4j:twitch4j:${project.properties["twitch4j_version"]}")

    // Config
    modApi("me.shedaniel.cloth:cloth-config-fabric:${project.properties["cloth_config_version"]}")

    modImplementation("com.terraformersmc:modmenu:${project.properties["mod_menu_version"]}")

    modImplementation("org.teamvoided:voidlib-core:1.5.2+1.20.1")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.version))
        }
    }

    // Minecraft 1.18 (1.18-pre2) upwards uses Java 17.
    val targetJavaVersion = 17
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = targetJavaVersion.toString()
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(targetJavaVersion).toString()))
        withSourcesJar()
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName}" }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {}
}