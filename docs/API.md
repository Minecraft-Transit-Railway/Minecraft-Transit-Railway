# API

> Local HTTP endpoints exposed by Minecraft Transit Railway helper servlets.

## Scope and assumptions

- These endpoints are served by embedded/local servlet handlers in the mod runtime.
- Most routes are intended for in-game UI tools, not for external public internet exposure.
- Behaviour described here is based on current handlers in `common/src/main/java/org/mtr/servlet/`.

## Servlet map

| Servlet class                         | Main purpose                                                                                |
|---------------------------------------|---------------------------------------------------------------------------------------------|
| `ClientServlet`                       | Proxies browser/UI requests through the Minecraft client-server packet bridge.              |
| `ResourcePackCreatorOperationServlet` | Handles resource-pack-creator actions such as refresh, preview, and sound preview controls. |
| `ResourcePackCreatorUploadServlet`    | Handles resource import/export/reset flows for the resource pack creator.                   |

## `ClientServlet`

### Methods

- `GET`
- `POST`

### Behaviour

- Captures the requested endpoint URI.
- Forwards the request payload (for `POST`) over a packet bridge.
- Returns:
  - `200 OK` with response payload when the endpoint resolves directly.
  - Redirect response when backend resolves a different path.

## `ResourcePackCreatorOperationServlet`

### GET paths

| Path suffix     | Behaviour                                                                        |
|-----------------|----------------------------------------------------------------------------------|
| `/refresh`      | Returns current temporary resource wrapper state (or `null` payload when empty). |
| `/play-sound`   | Previews vehicle sounds based on query parameters.                               |
| `/preview`      | Adjusts preview state (for example door open/close direction).                   |
| `/force-reload` | Triggers resource reload UI flow and reload callback.                            |
| `/resume-game`  | Returns to gameplay screen and responds immediately.                             |

### POST paths

| Path suffix | Behaviour                                                                                   |
|-------------|---------------------------------------------------------------------------------------------|
| `/update`   | Replaces temporary resource wrapper from posted JSON payload and returns standard response. |

### Notable query parameters

`/play-sound` supports:

- `id`
- `type` (`bve`, `legacy`)
- `mode` (`speed`, `door-open`, `door-close`)
- `value`
- `speed-sound-count`
- `use-acceleration-sounds-when-coasting`
- `constant-playback-speed`

## `ResourcePackCreatorUploadServlet`

### GET paths

| Path suffix | Behaviour                                                                                       |
|-------------|-------------------------------------------------------------------------------------------------|
| `/reset`    | Clears temporary upload state and reloads custom resources.                                     |
| `/export`   | Exports current temporary resources as a resource-pack zip in the local `resourcepacks` folder. |

### POST paths

| Path suffix  | Behaviour                                                              |
|--------------|------------------------------------------------------------------------|
| `/zip`       | Uploads a zip and imports supported resources into temporary state.    |
| `/resources` | Uploads one or more standalone resources into current temporary state. |

### Upload handling notes

- Supported imported resources include `.png`, `.bbmodel`, `.obj`, `.mtl`, and `.json` metadata where applicable.
- Export writes generated content and `pack.mcmeta`, then opens the local resource-pack folder.

## Response semantics

- Standard success responses return `200 OK` with JSON payload (or `null`-like body in some flows).
- Validation or unsupported path cases return `400 BAD_REQUEST`.
- Internal errors are logged and generally surface as error responses.

## Related runtime entry points

- Message fan-out and response processing: `common/src/main/java/org/mtr/servlet/MinecraftOperationProcessor.java`
- Client/server packet bridge abstractions: `common/src/main/java/org/mtr/packet/`
