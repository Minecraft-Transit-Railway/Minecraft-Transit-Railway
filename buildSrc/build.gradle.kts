repositories {
	mavenCentral()
	flatDir { dirs("../libs") }
	maven { url = uri("https://jitpack.io") }
	maven {
		url = uri("https://maven.pkg.github.com/Minecraft-Transit-Railway/Transport-Simulation-Core")
		credentials {
			username = providers.gradleProperty("gpr.user").getOrNull() ?: "github-actions"
			password = providers.gradleProperty("gpr.key").getOrNull() ?: System.getenv("GITHUB_TOKEN")
		}
	}
}

dependencies {
	implementation("com.google.code.gson:gson:+")
	implementation("com.github.crowdin:crowdin-api-client-java:+")
	implementation("it.unimi.dsi:fastutil:+")
	implementation("commons-io:commons-io:2.+")
	implementation("org.apache.httpcomponents:httpmime:+")
	implementation("org.mtr:Minecraft-Mod-API-Tools:0.0.1")
	implementation("org.mtr:transport-simulation-core-build-tools:+")
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}
