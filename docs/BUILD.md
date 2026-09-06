# Build

> How to set up, build, and validate Minecraft Transit Railway from source.

## Prerequisites

| Tool        | Version / notes                                                                                                                     |
|-------------|-------------------------------------------------------------------------------------------------------------------------------------|
| JDK         | **25** required to run the Gradle build. Stonecutter raised its floor to JVM 25, so Gradle itself refuses to start on Java 21.      |
| Node.js     | Current LTS recommended for `website/` development tasks.                                                                           |
| npm         | Bundled with Node.js.                                                                                                               |
| Gradle      | Use the wrapper scripts in this repo (`gradlew` / `gradlew.bat`).                                                                   |
| Stonecutter | Integrated into Gradle build; no separate installation required. Manages multi-platform (Fabric/NeoForge) and multi-version builds. |

### Transport Simulation Core

The build resolves two artefacts from the [Transport Simulation Core](https://github.com/Minecraft-Transit-Railway/Transport-Simulation-Core)
project: `org.mtr:transport-simulation-core` for the mod itself, and
`org.mtr:transport-simulation-core-build-tools` for the schema generator used by `buildSrc`.

Both are published to GitHub Packages, which requires an access token for reads **even though the
repository is public**. Continuous integration is unaffected, because GitHub Actions injects
`GITHUB_TOKEN` automatically. Local development has two options.

#### Option 1: build it from source (no credentials)

Clone the project and publish both artefacts to your local Maven repository. The project name has to
be lowercased first, because the published artefact ids are lowercase while the Gradle project name
is not:

```powershell
git clone https://github.com/Minecraft-Transit-Railway/Transport-Simulation-Core.git
cd Transport-Simulation-Core
echo 'rootProject.name = "transport-simulation-core"' > settings.gradle.kts
echo 'rootProject.name = "transport-simulation-core-build-tools"' > buildSrc/settings.gradle.kts
.\gradlew.bat publishToMavenLocal -x test
cd buildSrc; ..\gradlew.bat publishToMavenLocal
```

The build prefers these local copies. The lookup is restricted to those two modules, so every other
dependency still resolves from its canonical remote rather than a stale local artefact.

#### Option 2: use a personal access token

Create a GitHub [personal access token (classic)](https://github.com/settings/tokens) with at least
the `read:packages` scope, then add it to your Gradle user-home properties file:

`C:\Users\<you>\.gradle\gradle.properties`

```properties
gpr.user=<your-github-username>
gpr.key=<your-personal-access-token>
```

Gradle also accepts the `GITHUB_TOKEN` environment variable. The GitHub Packages repository is only
declared when one of these is present, so a checkout without credentials falls back to Option 1
instead of failing during configuration.

## First-time setup

This project uses **Stonecutter** to manage multi-platform (Fabric/NeoForge) and multi-version (1.21.1, 1.21.4) builds from a single codebase. Platform-specific code is guarded with Stonecutter directives (e.g., `//? if fabric {` and `//? if neoforge {`). The active version is set in `stonecutter.gradle.kts` (default: `1.21.4-fabric`); the VCS base version is `1.21.1-fabric`.

From the repository root:

```powershell
.\gradlew.bat setupFiles
.\gradlew.bat setupWebsiteFiles
```

What these tasks do:

- `setupFiles`
	- Creates tokenised source/resource files from templates.
	- Pulls translation and supporting build assets where configured.
	- Generates Java schema classes and updates relocated imports.
	- Runs webserver setup support used by the mod.
- `setupWebsiteFiles`
	- Generates TypeScript entities into `website/src/app/entity/generated/`.

## Build outputs

This project uses **Stonecutter** for multi-platform (Fabric + NeoForge) and multi-version builds.
All supported version/loader combinations are listed in `settings.gradle.kts`:

| Minecraft version | Loader   | Stonecutter project ID |
|-------------------|----------|------------------------|
| 1.21.1            | Fabric   | `1.21.1-fabric`        |
| 1.21.1            | NeoForge | `1.21.1-neoforge`      |
| 1.21.4            | Fabric   | `1.21.4-fabric`        |
| 1.21.4            | NeoForge | `1.21.4-neoforge`      |

Active versions are configured in the `versions/` directory.

### Build current active version

```powershell
.\gradlew.bat build
```

Expected outputs:

- `build/release/MTR-fabric-<modVersion>+<minecraftVersion>.jar`
- `build/release/MTR-neoforge-<modVersion>+<minecraftVersion>.jar`

The version values come from `gradle.properties` (`modVersion` and `minecraftVersion`).

### Build specific version and loader

To build a specific Minecraft version for a specific loader:

```powershell
# 1.21.4 — Fabric
.\gradlew.bat "Set active project to 1.21.4-fabric"
.\gradlew.bat 1.21.4-fabric:build

# 1.21.4 — NeoForge
.\gradlew.bat "Set active project to 1.21.4-neoforge"
.\gradlew.bat 1.21.4-neoforge:build

# 1.21.1 — Fabric
.\gradlew.bat "Set active project to 1.21.1-fabric"
.\gradlew.bat 1.21.1-fabric:build

# 1.21.1 — NeoForge
.\gradlew.bat "Set active project to 1.21.1-neoforge"
.\gradlew.bat 1.21.1-neoforge:build
```

Available versions are in `versions/` (e.g., `versions/1.21.4-fabric/`, `versions/1.21.1-neoforge/`).

## Useful module tasks

```powershell
.\gradlew.bat :common:build
.\gradlew.bat :fabric:build
.\gradlew.bat :neoforge:build
```

## Website development

From `website/`:

```powershell
npm install
npm run start
```

Build frontend assets:

```powershell
npm run build
```

## Test and verification

Run Java tests declared in `common/`:

```powershell
.\gradlew.bat :common:test
```

If you only changed docs or comments, a full build is optional, but a quick `:common:test` run is still recommended before release branches.

## Common failures

- **`Dependency requires at least JVM runtime version 21`**
	- Gradle is running on Java 17 (or older). Point `JAVA_HOME` to a JDK 21 installation.
- **Generated files missing after checkout**
	- Re-run `setupFiles` and `setupWebsiteFiles`.
- **Website model type errors after schema edits**
	- Re-run `setupWebsiteFiles` and rebuild the website.
- **Build fails with platform/version mismatch**
	- Ensure you've set the active project before running build tasks (e.g., `"Set active project to 1.21.4-fabric"`).
	- Verify the version directory exists in `versions/` matching your target.
- **IDE shows compilation errors after checkout**
	- Stonecutter directives (e.g., `//? if fabric {`) may not be recognized by your IDE's language server.
	- Run Gradle tasks to generate the correct variant, or configure IDE to understand Stonecutter syntax.

## Related docs

- Runtime usage and packaging: [RUNNING.md](RUNNING.md)
- Endpoint details: [API.md](API.md)
- Schema generation details: [SCHEMA.md](SCHEMA.md)
