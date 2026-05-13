# Running

> Runtime and packaging notes for Minecraft Transit Railway development builds.

## Build artifacts

A full build creates loader-specific jars under `build/release/`:

- `MTR-fabric-<modVersion>+<minecraftVersion>.jar`
- `MTR-neoforge-<modVersion>+<minecraftVersion>.jar`

See [BUILD.md](BUILD.md) for build commands and prerequisites.

## Installing a development build

1. Build the project with Gradle.
2. Pick the jar matching your loader (`fabric` or `neoforge`).
3. Copy the jar into the target Minecraft instance `mods` directory.
4. Start Minecraft with matching loader and game version.

## Dedicated server considerations

- Use the server-compatible loader/runtime for your selected platform.
- Keep mod, loader, and Minecraft versions aligned with `gradle.properties`.
- If the transport backend becomes unresponsive, the mod currently logs an error and can stop the server to avoid desynchronised state.

## Local web tooling

MTR includes local helper endpoints for map and resource-pack-creator workflows.

- Endpoint behaviour is documented in [API.md](API.md).
- These routes are designed for local/in-game tooling and should not be exposed publicly without your own hardening.

## Troubleshooting runtime startup

- **Game fails with dependency/version mismatch**
  - Confirm loader, Minecraft version, and MTR jar variant match.
- **Missing generated or translated content in a dev build**
  - Re-run setup tasks from [BUILD.md](BUILD.md) before building.
- **Client-side web features unavailable**
  - Confirm client webserver startup in logs and check modpack/network constraints.
