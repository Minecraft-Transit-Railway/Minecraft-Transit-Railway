# Architecture

> A high-level view of how Minecraft Transit Railway is structured. For coding conventions,
> see [CODE_STYLES.md](CODE_STYLES.md). For endpoint details, see [API.md](API.md).

## Repository layout

| Module      | Purpose                                                                                                                                                |
|-------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| `common/`   | Shared mod logic used by all loaders: gameplay objects, packets, rendering helpers, servlets, registry abstractions, generated resource/config models. |
| `fabric/`   | Fabric-specific bootstrap and adapter code. Produces the Fabric mod jar.                                                                               |
| `neoforge/` | NeoForge-specific bootstrap and adapter code. Produces the NeoForge mod jar.                                                                           |
| `buildSrc/` | Build utilities, schema generator integration, translation/resource preparation tasks, and import-fix utilities used during build.                     |
| `website/`  | Angular web application and generated TypeScript entities used for map and data views.                                                                 |
| `docs/`     | Project documentation for architecture, build flow, runtime, API surface, and schema generation.                                                       |

## Runtime model

MTR runtime behaviour is split into three cooperating layers:

1. **Minecraft loader/runtime layer** (`fabric/`, `neoforge/`)
   - Initialises the mod for the selected loader.
   - Delegates gameplay logic into shared code in `common/`.

2. **Shared mod layer** (`common/`)
   - Owns blocks, items, entities, packets, rendering support, and player-world interactions.
   - Bridges selected requests to the transport backend via message passing.
   - Hosts local servlet endpoints used by in-game tools such as the resource pack creator.

3. **Transport backend integration** (`org.mtr:transport-simulation-core` dependency)
   - Handles simulation-heavy logic and data processing.
   - Exchanges operation messages with the shared mod layer.

## Message and data flow

- Player/client interactions trigger shared mod handlers in `common/`.
- Packet flows route Minecraft client/server requests via classes in `common/src/main/java/org/mtr/packet/`.
- Operation responses are post-processed in `common/src/main/java/org/mtr/servlet/MinecraftOperationProcessor.java` and fan out to players as needed.
- Persistent Minecraft-only settings are stored in `common/src/main/java/org/mtr/data/PersistentStateData.java`.

## Resource and schema flow

- Authoring schemas live under `buildSrc/src/main/resources/schema/`.
- Build tasks generate Java and TypeScript model classes from schema definitions.
- Generated Java code lands in `common/src/main/java/org/mtr/generated/`.
- Generated TypeScript code lands in `website/src/app/entity/generated/`.

See [SCHEMA.md](SCHEMA.md) for the regeneration workflow and guardrails.

## HTTP/servlet surface inside the mod

Servlet handlers for local tooling live in `common/src/main/java/org/mtr/servlet/`, including:

- `ClientServlet`
- `ResourcePackCreatorOperationServlet`
- `ResourcePackCreatorUploadServlet`

The endpoint matrix and method/path behaviour are documented in [API.md](API.md).

## Extension points

- Add new shared gameplay systems in `common/` first, then wire loader-specific hooks in `fabric/` and `neoforge/` only when required.
- For new wire formats or content models, update schema definitions first, then regenerate classes.
- Keep Minecraft-only concerns in `common/`, and simulation-core-only concerns in the backend dependency.
