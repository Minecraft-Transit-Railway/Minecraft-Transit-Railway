# Build

> How to set up, build, and validate Minecraft Transit Railway from source.

## Prerequisites

| Tool    | Version / notes                                                                                                                 |
|---------|---------------------------------------------------------------------------------------------------------------------------------|
| JDK     | **21** required to run the Gradle build in this repository. Running Gradle on Java 17 fails because `buildSrc` requires JVM 21. |
| Node.js | Current LTS recommended for `website/` development tasks.                                                                       |
| npm     | Bundled with Node.js.                                                                                                           |
| Gradle  | Use the wrapper scripts in this repo (`gradlew` / `gradlew.bat`).                                                               |

## First-time setup

From the repository root:

```powershell
.\gradlew.bat :common:setupFiles
.\gradlew.bat :common:setupWebsiteFiles
```

What these tasks do:

- `:common:setupFiles`
  - Creates tokenised source/resource files from templates.
  - Pulls translation and supporting build assets where configured.
  - Generates Java schema classes and updates relocated imports.
  - Runs webserver setup support used by the mod.
- `:common:setupWebsiteFiles`
  - Generates TypeScript entities into `website/src/app/entity/generated/`.

## Build outputs

Build all modules:

```powershell
.\gradlew.bat build
```

Expected outputs:

- `build/release/MTR-fabric-<modVersion>+<minecraftVersion>.jar`
- `build/release/MTR-neoforge-<modVersion>+<minecraftVersion>.jar`

The version values come from `gradle.properties` (`modVersion` and `minecraftVersion`).

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
  - Re-run `:common:setupFiles` and `:common:setupWebsiteFiles`.
- **Website model type errors after schema edits**
  - Re-run `:common:setupWebsiteFiles` and rebuild the website.

## Related docs

- Runtime usage and packaging: [RUNNING.md](RUNNING.md)
- Endpoint details: [API.md](API.md)
- Schema generation details: [SCHEMA.md](SCHEMA.md)
