import org.apache.tools.ant.filters.ReplaceTokens
import org.mtr.BuildTools
import org.mtr.core.Generator
import org.mtr.core.WebserverSetup

val modVersion: String by project
val minecraftVersion: String by project
val fabricLoaderVersion: String by project
val universalCraftVersion: String by project
val debug: String by project
val crowdinApiKey: String by project
val patreonApiKey: String by project
val buildTools = BuildTools(minecraftVersion, "fabric", project)

architectury {
	common(listOf("fabric", "neoforge"))
}

dependencies {
	modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
	compileOnly("gg.essential:universalcraft-standalone:$universalCraftVersion")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.+")
	testImplementation("org.junit.platform:junit-platform-launcher:1.+")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.+")
}

tasks.register("setupWebsiteFiles") {
	Generator.generateTypeScript(project, "schema/resource", "../website/src/app/entity/generated")
}

tasks.register("setupFiles") {
	copy {
		outputs.upToDateWhen { false }
		from("src/main/KeysTemplate.java")
		into("src/main/java/org/mtr")
		filter<ReplaceTokens>(mapOf("tokens" to mapOf("version" to "$modVersion+$minecraftVersion", "debug" to debug)))
		rename("(.+)Template.java", "$1.java")
	}

	copy {
		outputs.upToDateWhen { false }
		from("src/main/fabric.mod.template.json")
		into("src/main/resources")
		filter<ReplaceTokens>(mapOf("tokens" to mapOf("minecraft" to minecraftVersion, "version" to modVersion)))
		rename("(.+).template.json", "$1.json")
	}

	buildTools.downloadTranslations(crowdinApiKey)
	buildTools.generateTranslations()
	buildTools.copyVehicleTemplates()
	buildTools.getPatreonList(patreonApiKey)
	buildTools.setupObjLibrary()
	Generator.generateJava(project, "schema/config", "generated/config", false, "config")
	Generator.generateJava(project, "schema/resource", "generated/resource", false, "core.data", "resource")
	Generator.generateJava(project, "schema/legacy", "legacy/generated/resource", false)
	WebserverSetup.setup(project.rootDir, "common/", "")
	buildTools.fixImports(project, "generated")
	buildTools.fixImports(project, "generated/config")
	buildTools.fixImports(project, "generated/resource")
	buildTools.fixImports(project, "legacy/generated/resource")
}
