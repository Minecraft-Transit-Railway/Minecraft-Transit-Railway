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
- [Pending Java 21 features](docs/PENDING.md)
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

The [Crowdin site for Minecraft Transit Railway](https://crwd.in/minecraft-transit-railway) is available.

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

To build the mod, run the following commands in the root directory of the project:

```gradle
gradlew setupFiles -PminecraftVersion="<minecraft version>"
gradlew build -PminecraftVersion="<minecraft version>"
```

The mod JAR file should be generated in the following directory:

```
<root>/build/release/MTR-<fabric|forge>-<mod version>+<minecraft version>.jar
```

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
