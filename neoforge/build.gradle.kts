import org.mtr.BuildTools

plugins {
	id("com.gradleup.shadow")
}

val minecraftVersion: String by project
val neoforgeVersion: String by project
val universalCraftVersion: String by project
val elementaVersion: String by project
val buildTools = BuildTools(minecraftVersion, "neoforge", project)

architectury {
	platformSetupLoomIde()
	neoForge()
}

configurations {
	create("common") {
		isCanBeResolved = true
		isCanBeConsumed = false
	}

	val common by configurations
	compileClasspath.get().extendsFrom(common)
	runtimeClasspath.get().extendsFrom(common)
	configurations.getByName("developmentNeoForge").extendsFrom(common)

	create("shadowBundle") {
		isCanBeResolved = true
		isCanBeConsumed = false
	}
}

repositories {
	maven {
		name = "NeoForged"
		url = uri("https://maven.neoforged.net/releases")
	}
}

dependencies {
	neoForge("net.neoforged:neoforge:$neoforgeVersion")
	modImplementation("gg.essential:universalcraft-$minecraftVersion-neoforge:$universalCraftVersion")
	add("shadowBundle", "org.mtr:transport-simulation-core:+")
	add("shadowBundle", "com.logisticscraft:occlusionculling:+")
	add("shadowBundle", "gg.essential:elementa:$elementaVersion")
	add("shadowBundle", "gg.essential:universalcraft-$minecraftVersion-neoforge:$universalCraftVersion")
	add("common", project(path = ":common", configuration = "namedElements")) {
		isTransitive = false
	}
	add("shadowBundle", project(path = ":common", configuration = "transformProductionNeoForge"))
}

tasks.named<ProcessResources>("processResources") {
	inputs.property("version", project.version)

	filesMatching("META-INF/neoforge.mods.toml") {
		expand(mapOf("version" to project.version))
	}
}

tasks.named("build") {
	doLast {
		buildTools.copyBuildFile()
	}
}

tasks.shadowJar {
	configurations = listOf(project.configurations["shadowBundle"])
	archiveClassifier.set("dev-shadow")
	minimize {
		exclude(dependency("org.mtr:Shadow-Libraries-net:.*"))
	}
	relocate("com.logisticscraft", "org.mtr.libraries.com.logisticscraft")
	relocate("de.javagl", "org.mtr.libraries.de.javagl")
	relocate("gg.essential", "org.mtr.libraries.gg.essential")
}

tasks.remapJar {
	inputFile.set(tasks.shadowJar.get().archiveFile)
}
