pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://maven.kikugie.dev/releases")
		maven("https://maven.kikugie.dev/snapshots")
		maven("https://maven.fabricmc.net/")
		maven("https://maven.neoforged.net/releases/")
	}
}

plugins {
	id("dev.kikugie.stonecutter") version "+"
	id("org.gradle.toolchains.foojay-resolver-convention") version "+"
}

stonecutter {
	create(rootProject) {
		mapBuilds { _, node ->
			"build-${node.project.substringAfter('-')}.gradle.kts"
		}

		versions(
			"1.21.1-fabric" to "1.21.1",
			"1.21.1-neoforge" to "1.21.1",
			"1.21.4-fabric" to "1.21.4",
			"1.21.4-neoforge" to "1.21.4",
		)

		vcsVersion = "1.21.4-fabric"
	}
}

rootProject.name = "Minecraft-Transit-Railway"
