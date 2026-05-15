# Runtime Model Loading and Rendering Pipeline

> Detailed reference for the client-side model-loading and rendering path used by MTR 4.x.
> This document focuses on the hot paths that affect frame time and first-encounter lag.
>
> Related docs:
> - Performance findings and backlog: [PERFORMANCE.md](PERFORMANCE.md)
> - Project architecture overview: [ARCHITECTURE.md](ARCHITECTURE.md)
> - Migration and cleanup tracking: [MIGRATIONS.md](MIGRATIONS.md), [PENDING.md](PENDING.md)

## 1. High-level pipeline

At runtime, model data goes through five stages:

1. **Resource reload and registration**
   - Entry point: `common/src/main/java/org/mtr/client/CustomResourceLoader.java` -> `reload()`.
   - Clears caches, reads resource-pack manifests, creates `VehicleResource`/`RailResource`/`ObjectResource`/`LiftResource` entries.

2. **Model-loader dispatch (OBJ vs Blockbench)**
   - Vehicle path: `common/src/main/java/org/mtr/resource/VehicleModel.java`.
   - Rail/object path: `common/src/main/java/org/mtr/resource/StoredModelResourceBase.java`.
   - Dispatches to `ObjModelLoader` or `BlockbenchModelLoader`.

3. **Async parsing on worker threads**
   - Parse work is submitted to `MainRenderer.WORKER_THREAD.worker`.
   - OBJ: `ObjReader.read(...)`, `MtlReader.read(...)`, face walk -> `addModel(...)`.
   - BBModel: JSON parse + outline walk -> `addModel(...)`.

4. **Synchronous GPU upload (render thread)**
   - `ModelLoaderBase.get()` eventually triggers optimised model build and VBO upload.
   - Upload path includes `NewOptimizedModelGroup.build()` -> `VertexBuffer.createAndUpload(...)`.
   - This is render-thread-only due to GL context requirements.

5. **Per-frame render queueing and draw drain**
   - World systems queue work via `MainRenderer.scheduleRender(...)` or `MainRenderer.renderModel(...)`.
   - End-of-frame drain batches by layer/texture/model and emits draw calls.

## 2. Reload and resource registration

### Entry point and responsibilities

`CustomResourceLoader.reload()` performs:

- cache reset (`RESOURCE_CACHE`, resource lists/maps)
- dynamic texture cache reload
- default rail registration
- parsing of `mtr_custom_resources.json`
- parsing of temporary pending-migration resources
- conversion/import of legacy rails and objects
- duplicate-ID validation and load summary logging
- preload pass for resources matched by preload pattern

### Resource content cache

- `RESOURCE_CACHE` is currently a `ConcurrentHashMap<String, String>` keyed by resource identifier.
- Reads use `computeIfAbsent(...)` so concurrent parse tasks do not fetch/parse the same file repeatedly.
- Cache is thread-safe for async OBJ/BBModel loading.

## 3. Async parse coordination and preload behaviour

### Why parse and upload are split

- **CPU parse** is safe on workers and can scale with virtual threads.
- **GPU upload** requires the Minecraft render thread and cannot be parallelized blindly.

### Parse lifecycle tracking

`ModelLoaderBase` tracks global parse progress:

- `parseStarted()` increments a shared pending counter.
- `parseFinished()` decrements in `finally` blocks.
- `awaitParsing(timeoutMillis)` blocks until pending parse count reaches zero, or timeout.

OBJ and Blockbench loaders call this lifecycle around worker-submitted parse jobs.

### Reload ordering improvement

Before preload begins, `CustomResourceLoader.reload()` now calls:

- `ModelLoaderBase.awaitParsing(60_000L)`

This ensures preload does not race with unfinished parse tasks. Without this wait, preload may skip models that are still parsing, which shifts parse/upload cost into a later render frame and causes first-encounter spikes.

## 4. Worker thread model

`common/src/main/java/org/mtr/render/WorkerThread.java` is the shared background coordinator.

### Dynamic texture queue

- Uses bounded FIFO (`ArrayDeque`) instead of single-slot overwrite.
- Work submission wakes the loop immediately (`LockSupport.unpark(...)`).
- Loop drains in batches (`DYNAMIC_TEXTURE_BATCH`) to balance throughput vs fairness.

### Occlusion tasks

- Vehicle/lift/rail occlusion queues remain last-writer-wins where that behaviour is intentional.

## 5. Core render pipeline

### Queue APIs

`MainRenderer` exposes two main paths:

1. `scheduleRender(...)`
   - For callback-based draws (quads, lines, text, overlays) grouped by texture + render layer.
2. `renderModel(...)`
   - For prebuilt `NewOptimizedModel` instances grouped by `RenderStage` and light.

### Frame stages (simplified)

1. world systems enqueue render work
2. queue maps are swapped/current-frame snapshots taken
3. opaque/translucent model buckets are drained
4. queued layer draws are drained by priority and layer ordering
5. queues are cleared for next frame

### Hot-path micro-optimisations currently in place

- cached enum arrays in `MainRenderer` to avoid repeated `values()` allocations
- constant empty identifier for non-texture `scheduleRender(...)` calls
- reduced temporary allocations in `NewOptimizedModel`:
  - reusable model-view matrix scratch
  - quantised colour-modulator table for common light values

## 6. Rail rendering path and distance behaviour

### Current flow

Rail rendering happens in `common/src/main/java/org/mtr/render/RenderRails.java`.

- rail selection and mode dispatch (`TRAIN`/`BOAT`/`CABLE_CAR`/`AIRPLANE`)
- textured rail quads and style-specific 3D model rendering
- signals and one-way overlays

### Per-rail culling change

Previous behaviour culled per segment while iterating `rail.railMath.render(...)`, which produced:

- many per-segment distance checks and allocations
- visible cutoffs where long rails ended abruptly once far segments left budget

Current behaviour culls each rail once via AABB distance:

- helper: `CullingHelper.getDistanceFromCameraToBox(minX, minY, minZ, maxX, maxY, maxZ)`
- rail is either fully rendered or fully skipped
- removes mid-curve cutoff artifacts and avoids segment-walk cost for off-range rails

This addresses the visual problem of shortened custom render caps while preserving performance.

## 7. Known constraints and practical LOD direction

### Constraints

- GL uploads remain render-thread-bound.
- Generic, high-quality runtime mesh decimation for arbitrary assets is expensive.
- Aggressive runtime LOD generation can reintroduce CPU spikes if done on-demand in view.

### Practical LOD strategy for this codebase

1. **Rail LOD first (highest ROI)**
   - distance bands for interval/detail level
   - optional caching for far-rail geometry replay
2. **Model LOD with async warm-up**
   - generate lower-detail variants in worker after LOD0 parse
   - upload lazily on render thread without blocking gameplay
3. **Hysteresis in LOD switching**
   - avoid near-threshold thrash (different enter/leave distances)

## 8. Simplification roadmap (maintenance-focused)

The pipeline works, but several areas are still complex due to legacy migration and renderer constraints.

Recommended simplifications:

- unify duplicated OBJ/BB dispatch logic between `VehicleModel` and `StoredModelResourceBase`
- split `RenderRails.renderRailStandard(...)` into dedicated methods by concern
- reduce transform/lambda churn in hot loops (especially rail segment paths)
- keep `MainRenderer` queue semantics documented and predictable across overloads
- continue moving one-off temporary migration code behind clear boundaries and removal plans

Tracked in: [MIGRATIONS.md](MIGRATIONS.md), [PENDING.md](PENDING.md), and [PERFORMANCE.md](PERFORMANCE.md).

## 9. File index (quick jump)

- `common/src/main/java/org/mtr/client/CustomResourceLoader.java`
- `common/src/main/java/org/mtr/model/ModelLoaderBase.java`
- `common/src/main/java/org/mtr/model/ObjModelLoader.java`
- `common/src/main/java/org/mtr/model/BlockbenchModelLoader.java`
- `common/src/main/java/org/mtr/resource/VehicleModel.java`
- `common/src/main/java/org/mtr/resource/StoredModelResourceBase.java`
- `common/src/main/java/org/mtr/render/WorkerThread.java`
- `common/src/main/java/org/mtr/render/MainRenderer.java`
- `common/src/main/java/org/mtr/model/NewOptimizedModel.java`
- `common/src/main/java/org/mtr/render/RenderRails.java`
- `common/src/main/java/org/mtr/tool/CullingHelper.java`
