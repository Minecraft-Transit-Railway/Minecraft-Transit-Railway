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
	id("dev.kikugie.stonecutter") version "0.10-alpha.8"
	// Selects between Loom's remapping and non-remapping variants per version. Minecraft is
	// unobfuscated from 26.1 onwards, so the two cannot be served by a single Loom plugin id.
	id("dev.kikugie.loom-back-compat") version "0.4.2"
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
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
			"26.1.2-neoforge" to "26.1.2",
		)

		vcsVersion = "1.21.4-fabric"
	}
}

rootProject.name = "Minecraft-Transit-Railway"
