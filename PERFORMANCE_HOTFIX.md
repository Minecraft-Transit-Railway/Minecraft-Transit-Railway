# Fabric 1.20.1 Rail Rendering Performance Hotfix

This branch targets the client render-thread bottleneck caused by large numbers of MTR rails and station elements. The primary change caches eligible default rail geometry as static GPU meshes instead of tessellating and submitting the same rail vertices again on every frame.

## Scope

- Source base: upstream `4.0.6` branch at `240524a18d59edeb75cf8040cb209b91b428b5d7`.
- Validated runtime: Fabric 1.20.1.
- Exact tested artifact: `MTR-fabric-4.0.5+1.20.1-D3-GPU-mesh-hotfix.jar`.
- The upstream source line is named `4.0.6`, while its current `gradle.properties` still sets the built MTR version to `4.0.5`.
- The rail mesh cache is client-side and does not change the network protocol or world-save format. A matching server hotfix is not required for this rendering path.
- The GPU mesh path requires MTR's `OptimizedRenderer`; when optimized rendering is unavailable, rails use the legacy renderer.
- Forge and Minecraft versions other than Fabric 1.20.1 have not been validated for this release.

## Controlled D3 Benchmark

The benchmark used a local copy of the server's `world2` overworld and a fixed flight along the D3 track at Shuyuan North. Every accepted run was validated against the actual player coordinates; earlier captures where the player did not move were rejected.

- Commanded route: north `(186.5, 72, -389.5)` to south `(186.5, 72, -347.5)`. Accepted actual endpoints ranged from `z = -347.092` to `z = -346.548`, or approximately 42.41 to 42.95 blocks of forward movement.
- Requested traversal time: 4,450 ms.
- Camera: yaw 0 degrees, pitch 12 degrees.
- Repetitions: four accepted runs per build.
- Comparison: previous CPU-reduction hotfix `9DEF9575...` versus final GPU-mesh hotfix `8B80F1A4...`.
- Both builds used the same world, route, client profile, video settings, and measurement pipeline.

| Dynamic traversal metric | Previous hotfix | GPU-mesh hotfix | Change |
| --- | ---: | ---: | ---: |
| Whole route mean FPS | 27.66 | 32.62 | **+17.9%** |
| North third mean FPS | 25.07 | 31.28 | **+24.8%** |
| Middle third mean FPS | 27.43 | 31.45 | **+14.7%** |
| South third mean FPS | 30.46 | 35.14 | **+15.4%** |
| Mean whole-route p95 frame time | 44.46 ms | 43.53 ms | **-2.1%** |

The primary result is the four-run whole-route mean of **27.66 to 32.62 FPS (+17.9%)**. The first GPU-mesh run was retained rather than discarded; the three subsequent warm runs averaged approximately 34.0 FPS.

These numbers are workload- and profile-specific. Absolute FPS should not be generalized to other maps, view distances, GPUs, or modpacks.

### Test profile

- CPU: AMD Ryzen 9 8940HX.
- GPU: NVIDIA GeForce RTX 5060 Laptop GPU.
- Renderer: OpenGL 3.2, NVIDIA 596.49.
- Compatibility stack present during testing: JCM 2.2.3, Sodium 0.5.13, Sodium Extra 0.5.9, Entity Culling 1.9.0, and ImmediatelyFast 1.5.5.

### Prior field observation

Before the controlled local-map benchmark, the user observed FPS increasing from approximately 18 to 24 (+33.3%) while connected to the dedicated server at a very large station with an earlier modified build. That observation used a different build and did not have the same route, repetition, or coordinate validation. It is included as field context only and is not attributed to the final `8B80F1A4...` build or combined with the controlled result.

## Why It Helps

The original rail path runs `railMath.render`, visibility calculations, quad construction, `BufferBuilder`, and vertex submission for a large number of rail segments on the client render thread every frame. A single saturated render thread can therefore limit FPS even when total CPU and GPU utilization remain low.

The hotfix changes that path as follows:

1. Only normal, default-style, two-dimensional `TRAIN` rails enter the cache. Custom styles, multiple styles, 3D rails, edit previews, and unsupported transport modes keep the legacy renderer.
2. Rail geometry is still tessellated at 0.5 m intervals, but it is divided into pages of 128 segments, nominally 64 m per page.
3. Geometry preparation runs on one or two daemon worker threads, depending on available processors.
4. Prepared pages are uploaded on the render thread because OpenGL resources are render-context-bound. Uploads are limited to eight pages per frame with a soft approximately 2 ms per-frame budget after the first upload.
5. Uploaded pages are reused as static GPU meshes on later frames. The render thread then performs page visibility selection and queues existing meshes instead of rebuilding every rail quad.
6. Page-local coordinates preserve float precision at large world coordinates, and block/sky light is retained per rail segment when a page is uploaded.
7. The estimated mesh cache is capped at 128 MiB and evicts older entries when it exceeds that budget.
8. Rail geometry changes, removed rails, chunk loads, light-section updates, resource reloads, disconnects, resets, and world changes invalidate the affected cache state.
9. Until a page is ready, or if a rail is ineligible or preparation/upload fails, rendering falls back to the original path rather than omitting the rail.

This is not multithreaded OpenGL and it does not use NVIDIA-specific APIs. The gain comes from moving repeatable CPU geometry preparation off the render thread and, more importantly, retaining static geometry on the GPU so it is not rebuilt every frame.

JCM rail-level occlusion selection remains in place before cached rails are considered. Compatibility tests also preserve the compiler-generated MTR methods and local-variable layout used by JCM 2.2.3 mixins.

## Profiling Evidence

Matched Java Flight Recorder samples show that the controlled FPS change corresponds to removal of repeated rail work from the render thread:

| JFR render-thread metric | Previous hotfix | GPU-mesh hotfix | Relative change |
| --- | ---: | ---: | ---: |
| Samples under `RenderRails` | 34.41% | 2.70% | -92.2% |
| Samples under legacy `renderRailStandard` | 33.33% | 0.60% | -98.2% |
| Samples under `BufferBuilder.nextElement` | 16.13% | 0.30% | -98.1% |
| Sampled allocation volume attributed to rail rendering | 435.9 MiB | 6.0 MiB | -98.6% |

The first three rows are shares of JFR render-thread execution samples, not absolute CPU time. The allocation row is JFR sampled allocation volume, not total heap allocation.

## Supporting Changes

The branch also retains the preceding hotfix work around the mesh cache:

- Allocation-light fallback rail visibility math with equivalence tests around distance and camera-facing boundaries.
- No unused rail or vehicle occlusion-task construction during shadow passes.
- Coalesced asynchronous dynamic-texture generation, stale-result rejection, retry backoff, and explicit image/texture ownership.
- Fewer redundant route-panel block scans and duplicate APG route rendering.
- Nested optimized-renderer reload handling and GL state preservation for incremental mesh uploads.
- Reuse of serialized broadcast packet content instead of serializing once per player.

The controlled `+17.9%` comparison isolates the final GPU-mesh build from the previous hotfix build; it is not a measurement of every supporting change against unmodified upstream.

## Known Limits

- The 128 MiB cache limit is estimated accounting, not a strict VRAM or total-memory limit.
- An exceptionally long single rail is prepared before its estimated size is checked. It can therefore cause a temporary preparation-memory peak before being rejected and returned to the legacy renderer. The tested D3 scene did not trigger this case.
- Nearby-rail prewarming scans the currently known rail wrappers each frame. Extremely large networks can therefore retain some render-thread scaling cost even after mesh caching.
- Iris and other shader packs were not included in the validated profile.
- OpenGL upload remains render-thread work, and the first page uploaded in a frame can exceed the soft time budget. The cache removes the repeated per-frame cost after upload; it does not make initial upload free.

## Build And Verification

Build with Java 21:

```shell
./gradlew -PminecraftVersion=1.20.1 :fabric:clean :fabric:setupFiles :fabric:test :fabric:build
```

Final verification:

- Fabric clean build completed successfully.
- 38 tests passed with 0 failures and 0 errors.
- Exact rebuilt jar: `fabric/build/libs/fabric-4.0.5.jar`.
- SHA-256: `8B80F1A451F9DF6D5D892CDDB5B32C641098E4989B83A906FC2236A62DF470A0`.
- The rebuilt jar is byte-for-byte identical to the artifact used for the reported D3 benchmark.
