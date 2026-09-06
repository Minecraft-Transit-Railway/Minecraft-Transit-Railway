import org.apache.tools.ant.filters.ReplaceTokens
import org.mtr.BuildTools
import org.mtr.core.Generator
import org.mtr.core.WebserverSetup

plugins {
	id("net.fabricmc.fabric-loom-remap")
	id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.23"
	id("io.freefair.lombok") version "9.5.0"
	id("com.gradleup.shadow") version "9.6.1"
}

base.archivesName = property("mod.id") as String
version = "${property("mod.version")}+${sc.current.version}-fabric"

repositories {
	// Transport Simulation Core can be built from source and published locally, which is the only
	// route that works without GitHub credentials. Restricted to that single module so every other
	// dependency keeps resolving from its canonical remote instead of a stale local artifact.
	mavenLocal {
		content { includeModule("org.mtr", "transport-simulation-core") }
	}
	mavenCentral()
	maven { url = uri("https://repo.codemc.org/repository/maven-public") } // Occlusion Culling
	maven { url = uri("https://repo.essential.gg/repository/maven-public") } // Elementa and UniversalCraft
	maven { url = uri("https://api.modrinth.com/maven") }
	// GitHub Packages demands an access token even though Transport Simulation Core is a public
	// repository, so this repository is only declared once a token is actually available. Gradle
	// rejects a null password while configuring the project, which would otherwise abort every
	// credential-less build before it reached the locally published copy above.
	val githubPackagesToken = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GITHUB_TOKEN")
	if (githubPackagesToken != null) {
		maven {
			url = uri("https://maven.pkg.github.com/Minecraft-Transit-Railway/Transport-Simulation-Core")
			credentials {
				username = providers.gradleProperty("gpr.user").orNull ?: "github-actions"
				password = githubPackagesToken
			}
		}
	}
}

val buildTools = BuildTools(sc.current.version, "fabric", project.property("mod.version").toString(), project.rootDir)
// Minecraft 26.1 runs on Java 25, not 26: its version manifest pins the java-runtime-epsilon
// component to major version 25. Targeting 26 would emit class files the game's own runtime
// refuses to load, and the mismatch would only surface at launch rather than during the build.
val requiredJava = when {
	sc.current.parsed < "26.0" -> JavaVersion.VERSION_21
	else -> JavaVersion.VERSION_25
}

configurations {
	create("shadowBundle") {
		isCanBeResolved = true
		isCanBeConsumed = false
	}
}

java {
	withSourcesJar()
	targetCompatibility = requiredJava
	sourceCompatibility = requiredJava
}

fun DependencyHandlerScope.modImplementationAndInclude(notation: Any) {
	modImplementation(notation)
	include(notation)
}

fun DependencyHandlerScope.implementationAndInclude(notation: Any) {
	implementation(notation)
	include(notation)
}

fun DependencyHandlerScope.implementationAndShadow(notation: Any) {
	implementation(notation)
	add("shadowBundle", notation)
}

dependencies {
	minecraft("com.mojang:minecraft:${sc.current.version}")
	mappings(loom.officialMojangMappings())

	modImplementation("net.fabricmc:fabric-loader:${property("dependency.fabric_loader")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("dependency.fabric_api")}")
	modImplementation(fletchingTable.modrinth("modmenu", sc.current.version))
	modImplementationAndInclude("gg.essential:universalcraft-${property("dependency.universal_craft_minecraft")}-fabric:${property("dependency.universal_craft")}")

	implementationAndShadow("org.mtr:transport-simulation-core:1.0.2")
	// Occlusion Culling has only ever published snapshots, so this coordinate stays mutable even
	// though the version is fixed. Nothing newer than 0.0.8 exists to move to.
	implementationAndShadow("com.logisticscraft:occlusionculling:0.0.8-SNAPSHOT")
	implementationAndInclude("gg.essential:elementa:${property("dependency.elementa")}")
	implementationAndInclude("org.jetbrains.kotlin:kotlin-stdlib:2.4.10")
	implementation("org.jspecify:jspecify:1.0.0")

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.14.4")
	testImplementation("org.junit.platform:junit-platform-launcher:1.14.4")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.14.4")
}

tasks {
	processResources {
		val properties = mapOf(
			"mod_id" to project.property("mod.id"),
			"mod_name" to project.property("mod.name"),
			"mod_description" to project.property("mod.description"),
			"mod_license" to project.property("mod.license"),
			"mod_author" to project.property("mod.author"),
			"mod_version" to project.property("mod.version"),
			"mod_homepage" to project.property("mod.homepage"),
			"mod_sources" to project.property("mod.sources"),
			"mod_issues" to project.property("mod.issues"),
			"minecraft_version" to sc.current.version,
		)

		filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml")) {
			expand(properties)
		}

		exclude("**/neoforge.mods.toml")
	}

	test {
		useJUnitPlatform()
		testLogging { showStandardStreams = true }
	}

	javadoc {
		// Suppress "missing" doclint only (generated classes don't need javadoc)
		(options as StandardJavadocDocletOptions).addStringOption("Xdoclint:all,-missing", "-quiet")
	}

	shadowJar {
		configurations = listOf(project.configurations["shadowBundle"])
		minimize()
		relocate("com.logisticscraft", "org.mtr.libraries.com.logisticscraft")
		relocate("de.javagl", "org.mtr.libraries.de.javagl")
	}

	remapJar {
		inputFile.set(shadowJar.get().archiveFile)
	}

	withType<JavaCompile>().configureEach {
		options.compilerArgs.addAll(
			listOf(
				"-Xlint:all",
				"-Xlint:-serial",     // No Java serialization
				"-Xlint:-processing", // Lombok annotation processor noise
				"-Xlint:-this-escape" // Safe: schema constructor pattern
			)
		)
	}

	register<Copy>("buildAndCollect") {
		description = "Builds the mod and collects the JAR and sources JAR into the build/libs directory with versioned naming."
		group = "build"
		outputs.upToDateWhen { false }
		from(remapJar.map { it.archiveFile }, remapSourcesJar.map { it.archiveFile })
		into(rootProject.layout.buildDirectory.file("release"))
		rename("${project.property("mod.id")}-([^-]+)-([^-]+)-([a-z]+)(-sources|)\\.jar", "${project.property("mod.id").toString().uppercase()}-$3-$1-$2$4.jar")
		dependsOn("build")
	}

	register("setupWebsiteFiles") {
		description = "Generates TypeScript files for the website based on the resource schema."
		Generator.generateTypeScript(project, "schema/resource", "../../website/src/app/entity/generated")
	}

	register("setupFiles") {
		description = "Sets up necessary files for the mod, including generating Java classes from templates and processing translations."

		copy {
			outputs.upToDateWhen { false }
			from("../../src/main/KeysTemplate.java")
			into("../../src/main/java/org/mtr")
			filter<ReplaceTokens>(mapOf("tokens" to mapOf("version" to "${project.property("mod.version")}+${sc.current.version}", "debug" to "${project.property("debug")}")))
			rename("(.+)Template.java", "$1.java")
		}

		buildTools.downloadTranslations(project.property("key.crowdin").toString())
		buildTools.generateTranslations()
		buildTools.copyVehicleTemplates()
		buildTools.getPatreonList(project.property("key.patreon").toString())
		buildTools.setupObjLibrary()
		Generator.generateJava(project, "schema/config", "generated/config", "config")
		Generator.generateJava(project, "schema/resource", "generated/resource", "core.data", "resource")
		Generator.generateJava(project, "schema/legacy", "legacy/generated/resource")
		WebserverSetup.setup(project.rootDir, "", "")
		buildTools.fixImports(project, "generated/config")
		buildTools.fixImports(project, "generated/resource")
		buildTools.fixImports(project, "legacy/generated/resource")
	}
}
