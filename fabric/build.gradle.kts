import org.mtr.BuildTools

plugins {
	id("com.gradleup.shadow")
}

val minecraftVersion: String by project
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project
val modmenuVersion: String by project
val universalCraftVersion: String by project
val elementaVersion: String by project
val buildTools = BuildTools(minecraftVersion, "fabric", project)

architectury {
	platformSetupLoomIde()
	fabric()
}

configurations {
	create("common") {
		isCanBeResolved = true
		isCanBeConsumed = false
	}

	val common by configurations
	compileClasspath.get().extendsFrom(common)
	runtimeClasspath.get().extendsFrom(common)
	configurations.getByName("developmentFabric").extendsFrom(common)

	create("shadowBundle") {
		isCanBeResolved = true
		isCanBeConsumed = false
	}
}

dependencies {
	modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
	modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
	modImplementation("com.terraformersmc:modmenu:$modmenuVersion")
	modImplementation("gg.essential:universalcraft-$minecraftVersion-fabric:$universalCraftVersion")
	add("shadowBundle", "org.mtr:transport-simulation-core:+")
	add("shadowBundle", "com.logisticscraft:occlusionculling:+")
	add("shadowBundle", "gg.essential:elementa:$elementaVersion")
	include("gg.essential:universalcraft-$minecraftVersion-fabric:$universalCraftVersion")
	include("net.fabricmc:fabric-language-kotlin:+")
	add("common", project(path = ":common", configuration = "namedElements")) {
		isTransitive = false
	}
	add("shadowBundle", project(path = ":common", configuration = "transformProductionFabric"))
}

tasks.named<ProcessResources>("processResources") {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
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
	relocate("gg.essential.elementa", "org.mtr.libraries.gg.essential.elementa")
}

tasks.remapJar {
	inputFile.set(tasks.shadowJar.get().archiveFile)
}
