import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
	java
	`maven-publish`
	id("dev.architectury.loom") version "1.10-SNAPSHOT" apply false
	id("architectury-plugin") version "3.4-SNAPSHOT"
	id("com.gradleup.shadow") version "+" apply false
	id("io.freefair.lombok") version "+" apply false
}

val minecraftVersion: String by project
val modVersion: String by project
val yarnMappings: String by project
val yarnMappingsPatchNeoforgeVersion: String by project
val elementaVersion: String by project

architectury {
	minecraft = minecraftVersion
}

group = "org.mtr"
version = modVersion

subprojects {
	pluginManager.apply("dev.architectury.loom")
	pluginManager.apply("architectury-plugin")
	pluginManager.apply("maven-publish")
	pluginManager.apply("io.freefair.lombok")

	val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")

	base {
		archivesName.set("mtr-${project.name}")
	}

	repositories {
		mavenCentral()
		flatDir { dirs("../libs") }
		maven { url = uri("https://repo.codemc.org/repository/maven-public") }
		maven { url = uri("https://maven.terraformersmc.com/") }
		maven { url = uri("https://repo.essential.gg/repository/maven-public") }
	}

	dependencies {
		"minecraft"("net.minecraft:minecraft:$minecraftVersion")
		"mappings"(
			loom.layered {
				mappings("net.fabricmc:yarn:$yarnMappings:v2")
				mappings("dev.architectury:yarn-mappings-patch-neoforge:$yarnMappingsPatchNeoforgeVersion")
			}
		)
		implementation("com.google.code.findbugs:jsr305:+")
		implementation("org.mtr:Shadow-Libraries-net:0.0.1")
		implementation("org.mtr:Shadow-Libraries-util:0.0.1")
		implementation("org.mtr:Transport-Simulation-Core:0.0.1")
		implementation("com.logisticscraft:occlusionculling:+")
		implementation("gg.essential:elementa:$elementaVersion")
	}

	java {
		withSourcesJar()
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21
	}

	tasks.withType<JavaCompile>().configureEach {
		options.release.set(21)
	}

	publishing {
		publications {
			create<MavenPublication>("mavenJava") {
				artifactId = base.archivesName.get()
				from(components["java"])
			}
		}
		repositories {
		}
	}
}
