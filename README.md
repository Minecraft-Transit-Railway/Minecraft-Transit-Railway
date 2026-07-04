# Minecraft Transit Railway 4.0.0

_Minecraft Transit Railway_ is a [Minecraft mod](https://minecraft.wiki/w/Mods) that lets you build your own transport network with automated trains, boats, cable cars, and planes.

Version 4.0.0 is a complete rewrite from scratch.
The vehicle simulation code is separated from the Minecraft instance, which greatly reduces lag on Minecraft servers.

## Major Features in 4.0.0

- Custom train configuration
- Realistic bogies and train movement
- Improved signalling
- Sideways elevators
- OBJ features ported over from Nemo's Transit Expansion
- Vehicle swaying effect
- New online system map and OneBusAway integration
- Client-side FPS improvements and server-side TPS improvements

[See these features in detail](https://www.youtube.com/playlist?list=PLp0jZgheSlXuQCXu9UynrKuBnq2Ef_TSI).

[![Video Trailer](images/footer/video-preview.png)](https://www.youtube.com/watch?v=1cZfU7t4cAk)

Please report any issues or bugs you find; it is greatly appreciated.
Refer to the [to-do list](https://github.com/Minecraft-Transit-Railway/Minecraft-Transit-Railway/projects/2) to see currently known issues.

## Downloads and Installation

Visit the [Modrinth page](https://modrinth.com/mod/minecraft-transit-railway) to download the mod and view project information.

## Guide

There is a [new wiki](https://wiki.minecrafttransitrailway.com/start) for the mod.

## Documentation

- [API](docs/API.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Build](docs/BUILD.md)
- [Migrations](docs/MIGRATIONS.md)
- [Performance](docs/PERFORMANCE.md)
- [Pending work and progress log](docs/PENDING.md)
- [Runtime Model Loading and Rendering Pipeline](docs/RENDERING_PIPELINE.md)
- [Running and Testing](docs/RUNNING.md)
- [Schema](docs/SCHEMA.md)

## Recommended Addons and Resource Packs

- [MTR London Underground Addon](https://modrinth.com/mod/the-tube)
- [Joban Client Mod](https://modrinth.com/mod/jcm)
- [Station Decoration](https://modrinth.com/mod/station-decoration)
- [Russian Metro Addon](https://modrinth.com/mod/russian-metro-addon)
- [Tianjin Metro](https://modrinth.com/mod/tianjin-metro)

For a full list, check out [this site](https://addons.minecrafttransitrailway.com/).

## Contributing

### Help Translate the Mod!

The [Crowdin site for Minecraft Transit Railway](https://crowdin.com/project/minecraft-transit-railway) is available.

Crowdin is a cloud-based platform where translators can contribute to the project.
With your help, we can translate the mod into many different languages.
You can create a free account to start translating.

[![Crowdin](https://badges.crowdin.net/minecraft-transit-railway/localized.svg)](https://crowdin.com/project/minecraft-transit-railway)

### Adding Features

1. Fork this project
2. In your fork, create a new branch based on the development version branch
3. Commit your changes to the new branch
4. Open a Pull Request to merge your branch into the development version of this repository

> Please be sure to [sign all your GitHub commits](https://docs.github.com/en/authentication/managing-commit-signature-verification/signing-commits)!

### Building

To build the mod, this project uses **Stonecutter** for managing multi-platform (Fabric + NeoForge) and multi-version builds.

The build resolves `org.mtr:transport-simulation-core:+` from GitHub Packages. For local development, create a GitHub [personal access token (classic)](https://github.com/settings/tokens) with at least the `read:packages` scope, then add your credentials to your Gradle user-home properties file:

`C:\Users\<you>\.gradle\gradle.properties`

```properties
gpr.user=<your-github-username>
gpr.key=<your-personal-access-token>
```

As an alternative, Gradle will also use the `GITHUB_TOKEN` environment variable when it is set.

From the repository root:

```powershell
# Setup files (one-time)
.\gradlew.bat setupFiles -PcrowdinApiKey="<key>" -PpatreonApiKey="<key>"

# Set active version and build Fabric
.\gradlew.bat "Set active project to 1.21.4-fabric"
.\gradlew.bat 1.21.4-fabric:build

# Set active version and build NeoForge
.\gradlew.bat "Set active project to 1.21.4-neoforge"
.\gradlew.bat 1.21.4-neoforge:build
```

The mod JAR files should be generated in the following directory:

```
<root>/build/release/MTR-<fabric|neoforge>-<mod_version>+<minecraft_version>.jar
```

For full build instructions, see [docs/BUILD.md](docs/BUILD.md).

## Licence

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT). All fonts bundled with this mod are licensed under the [Open Font License](https://openfontlicense.org/).

- [Noto](https://fonts.google.com/noto)
- [正風毛筆字體](https://github.com/max32002/masafont)

## Questions? Comments? Complaints?

Let's connect.

<a href="https://discord.gg/PVZ2nfUaTW" target="_blank"><img src="https://github.com/Minecraft-Transit-Railway/Minecraft-Transit-Railway/blob/master/images/footer/discord.png" alt="Discord" width=64></a>
&nbsp;
<a href="https://www.linkedin.com/in/jonathanho33" target="_blank"><img src="https://github.com/Minecraft-Transit-Railway/Minecraft-Transit-Railway/blob/master/images/footer/linked_in.png" alt="LinkedIn" width=64></a>
&nbsp;
<a href="mailto:jonho.minecraft@gmail.com" target="_blank"><img src="https://github.com/Minecraft-Transit-Railway/Minecraft-Transit-Railway/blob/master/images/footer/email.png" alt="Email" width=64></a>
&nbsp;
<a href="https://www.patreon.com/minecraft_transit_railway" target="_blank"><img src="https://github.com/Minecraft-Transit-Railway/Minecraft-Transit-Railway/blob/master/images/footer/patreon.png" alt="Patreon" width=64></a>
