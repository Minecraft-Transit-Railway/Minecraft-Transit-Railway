repositories {
	// The schema generator ships as the Transport Simulation Core buildSrc artifact and is only
	// published to GitHub Packages, so a locally published copy is the sole credential-free route.
	// Restricted to that single module so every other build dependency still comes from Central.
	mavenLocal {
		content { includeModule("org.mtr", "transport-simulation-core-build-tools") }
	}
	mavenCentral()
	maven { url = uri("https://jitpack.io") }
	// Declared only when a token exists: Gradle rejects a null password during configuration, which
	// would otherwise abort the build before the locally published copy above could be consulted.
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

dependencies {
	implementation("com.google.code.gson:gson:2.14.0")
	implementation("com.github.crowdin:crowdin-api-client-java:1.34.0")
	implementation("it.unimi.dsi:fastutil:8.5.19")
	implementation("commons-io:commons-io:2.22.0")
	implementation("org.apache.httpcomponents:httpmime:4.5.14")
	implementation("org.mtr:transport-simulation-core-build-tools:1.0.2")
}
