# Performance Sweep — Findings and Action Plan

> A detailed inventory of inefficiencies, sources of lag, and concrete improvements identified
> by a full read-through of the client-side rendering and resource pipelines. Use this file
> as the source of truth when scheduling performance work. Items are grouped by the three
> reported pain points first, then by miscellaneous frame-time and memory issues found while
> tracing those paths.
>
> Each item carries:
> - **Where** — the file(s) and method(s) involved.
> - **Why it costs** — the mechanism that produces the lag, allocation, or stall.
> - **Fix** — the concrete refactor to apply, with enough detail to act on without re-reading
>   the surrounding code.
> - **Risk / scope** — how invasive the change is.
>
> Cross-references use `path:line-range` so an IDE "Go to Line" works directly.

---

## 1. Real-time resource loading (OBJ models and friends)

Resource loading happens on `/reload`, when the player joins a world with a custom resource
pack, when the resource pack creator submits a preview, and in the in-game previewer. The
hot path is `CustomResourceLoader.reload()`
([`common/src/main/java/org/mtr/client/CustomResourceLoader.java:60-187`](../common/src/main/java/org/mtr/client/CustomResourceLoader.java)).

### 1.1 Resource files are decoded to `String`, then re-encoded to `InputStream`

**Where**
- [`CustomResourceLoader.readResource(Identifier)`](../common/src/main/java/org/mtr/client/CustomResourceLoader.java) at lines 302–323.
- [`ObjModelLoader.loadModel(...)`](../common/src/main/java/org/mtr/model/ObjModelLoader.java) at lines 26–52, in particular `IOUtils.toInputStream(objString, StandardCharsets.UTF_8)`.
- [`VehicleModel`](../common/src/main/java/org/mtr/resource/VehicleModel.java) — every constructor does `Utilities.parseJson(resourceProvider.get(...))` and then `getModelLoaderBase(...)` which re-wraps strings.
- [`ResourceManagerHelper.readResource(Identifier) -> String`](../common/src/main/java/org/mtr/resource/ResourceManagerHelper.java) lines 32–42.

**Why it costs**

The model bytes are read from the resource pack as an `InputStream`, copied into a UTF-8
`String` (`IOUtils.toString(...)`), cached in `RESOURCE_CACHE` as a `String`, then handed to
`ObjReader.read(IOUtils.toInputStream(objString, UTF_8))`, which decodes the `String` back
to bytes. For a multi-megabyte `.obj`, this is two complete UTF-8 transcodes plus a full
heap copy. JSON files (`bbmodel`, properties, position-definitions) take the same trip
through `Utilities.parseJson`. Worse, every resource ever loaded stays pinned in
`RESOURCE_CACHE` (`Object2ObjectAVLTreeMap<String, String>`) for the lifetime of the JVM —
there is no eviction, so memory grows monotonically with the size of the active pack.

**Fix**

- Change `ResourceManagerHelper.readResource(Identifier, Consumer<InputStream>)` to be the
  primary API and route `Obj`/`bbmodel`/MTL readers directly through the `InputStream` —
  drop the intermediate `String`.
- Replace `RESOURCE_CACHE` with one of:
  1. A `byte[]` cache (`Object2ObjectOpenHashMap<String, byte[]>`) — same eviction policy as
     today but half the size of the UTF-16 `String` form, and avoids the encode round-trip.
  2. **Preferred**: drop the cache entirely. The vanilla resource manager already keeps
     resource handles open and reading from a zip is fast; we are double-caching.
- If a JVM-life-of-pack cache is needed (debug mode, see lines 306–315), evict on the
  Minecraft resource-reload event so a `/reload` actually frees memory.
- Make `Utilities.parseJson` accept a `Reader` so we can stream JSON without materialising
  the full `String`.

**Risk / scope**

Medium. The `resourceProvider.get(...) -> String` contract is threaded through
`VehicleModel`, `ObjectResource`, `RailResource`, `LiftResource`, `SignResource`, and the
schema-generated `ResourceProvider`. Migration path: introduce a parallel
`getInputStream(Identifier)` on `ResourceProvider`, switch model loaders one at a time,
then delete the `String` API once nothing calls it.

### 1.2 OBJ parsing happens on the calling thread, only mesh build is async

**Where** — [`ObjModelLoader.loadModel`](../common/src/main/java/org/mtr/model/ObjModelLoader.java) lines 26–52.

**Why it costs**

`ObjReader.read(...)` and `MtlReader.read(...)` run synchronously on whatever thread called
`VehicleModel`'s constructor — i.e. the main thread during `CustomResourceLoader.reload()`.
Only the subsequent `splitByGroups` / face iteration / `addModel` step is submitted to
`MainRenderer.WORKER_THREAD.worker`. For a 50k-vertex car body the `ObjReader.read` step
alone is 50–150 ms.

**Fix**

Move the full pipeline (`ObjReader.read` → `MtlReader.read` → split → faces → `addModel`)
inside the `worker.submit(...)` block. Note this requires the `mtlResolver` and
`textureResolver` callbacks to be thread-safe — they currently route through
`CustomResourceLoader.readResource` which writes to `RESOURCE_CACHE`. Either swap that map
for a `ConcurrentHashMap` or pre-resolve the strings on the main thread before submitting
(safer, no concurrency surface).

Apply the same change to `BlockbenchModelLoader` (`common/src/main/java/org/mtr/model/BlockbenchModelLoader.java`) — its `loadModel` currently parses the entire `.bbmodel` JSON on the caller thread.

**Risk / scope** — Low. The worker executor is `Executors.newVirtualThreadPerTaskExecutor()` so it scales fine.

### 1.3 Models are eagerly parsed even when never rendered

**Where**
- `VehicleModel` constructors (lines 30–68): every constructor runs `getModelLoaderBase(...)` which immediately invokes `ObjReader.read` / Blockbench parse.
- `CustomResourceLoader.reload()` iterates **every** vehicle (line 87) and constructs a `VehicleResource` for it; that resource constructs every `VehicleModel`; every `VehicleModel` parses its `.obj`/`.bbmodel`.

So on `/reload`, the cost is **N_vehicles × parse(model)** even though most vehicles are
never spawned in the current world. Only the `shouldPreload`-matching subset then **also**
runs `getCachedVehicleResource(0, 0)` (line 152) which is the optimised-mesh build.

**Why it costs**

For a typical operator pack with 200 vehicles × 3 model files each, this is 600 model
parses on the main thread before the world becomes interactive again. Joining a server with
a large pack shows a 5–30 s "Reloading resource packs…" freeze that is dominated by this
loop.

**Fix**

Defer the parse. `VehicleModel` should hold the `modelResource`/`textureResource` strings
plus a `Lazy<ModelLoaderBase>` (e.g. `Suppliers.memoize` style) and only construct the
loader the first time `builtVehicleModelHolderSupplier.get()` is actually called — which
already happens lazily inside `vehicleResourceCacheInitializer` (lines 291–304). The
existing `MODEL_LIFESPAN = 60000` (`VehicleModel:28`) suggests an unused-eviction pass was
intended but never wired up.

Pair this with §1.2: when the lazy initialiser runs, submit the full parse to
`WORKER_THREAD.worker` and return a "still loading" sentinel until the future resolves.
Renderers already cope with a `null` `BuiltVehicleModelHolder` — see
`VehicleResource.vehicleResourceCacheInitializer` lines 309–339 which returns `null` if any
sub-model is not yet built.

**Risk / scope** — Medium. Touches the contract that `VehicleModel.addToTextureResource`
(lines 78–117) and `toVehicleModelWrapper` (lines 119–125) need access to `ModelProperties`
**without** loading the mesh. Fix: parse `modelProperties.json` and `positions.json` eagerly
(they're tiny) but defer the mesh.

### 1.4 `RESOURCE_CACHE` is unbounded and pinned

Already covered by §1.1. Listed separately because the fix is independent: even if §1.1 is
not done, swapping the cache for a `Caffeine` or simple LRU with a size cap removes the
"memory grows forever" footgun. Recommended cap: 32 MiB; on overflow, evict the
least-recently-read entry.

### 1.5 `validateDataset` uses `ObjectOpenHashSet` of `String` ids but iterates as `List`

**Where** — [`CustomResourceLoader.validateDataset`](../common/src/main/java/org/mtr/client/CustomResourceLoader.java) lines 290–300.

Minor: works correctly but allocates strings for `getId.apply(data)` twice in the duplicate
case. Acceptable; flagged only for completeness.

### 1.6 Preload step is sequential and blocks main thread

**Where** — `CustomResourceLoader.reload()` lines 149–186.

The three preload loops (vehicles, rails, objects) run sequentially on the main thread.
Each call to `vehicleResource.getCachedVehicleResource(0, 0)` synchronously waits for the
mesh build. With §1.2 fixed, this loop should `submit` all preloads to the worker pool then
`await` them as a batch with a progress callback to Minecraft's loading screen.

**Fix**

```java
final ObjectArrayList<CompletableFuture<?>> preloadFutures = new ObjectArrayList<>();
VEHICLES.forEach((transportMode, vehicleResources) -> vehicleResources.forEach(vehicleResource -> {
	if (vehicleResource.shouldPreload) {
		preloadFutures.add(CompletableFuture.runAsync(() -> vehicleResource.getCachedVehicleResource(0, 0), MainRenderer.WORKER_THREAD.worker));
	}
}));
// similar for rails and objects, then:
CompletableFuture.allOf(preloadFutures.toArray(new CompletableFuture[0])).join();
```

The `join()` keeps the existing "block until preload done" semantics; the wins come from
parallelism across virtual threads.

**Risk / scope** — Low.

### 1.7 Debug-mode resource read does disk I/O per call

**Where** — `CustomResourceLoader.readResource` lines 306–315.

In `Keys.DEBUG`, every cache miss opens a `Files.newInputStream` from the source tree —
fine for dev, but it means the first frame after `/reload` does dozens of small reads. The
`RESOURCE_CACHE` mitigates repeats; with §1.4's eviction policy the dev experience would
not regress.

---

## 2. Dynamic texture generation (frame-time spikes)

The dynamic texture path is `DynamicTextureCache.getResource(...)` →
`MainRenderer.WORKER_THREAD.scheduleDynamicTextures(...)` →
`WorkerThread` polls → main-thread `registerTexture`.
([`DynamicTextureCache.java:243-321`](../common/src/main/java/org/mtr/client/DynamicTextureCache.java),
[`WorkerThread.java:30-88`](../common/src/main/java/org/mtr/render/WorkerThread.java).)

### 2.1 BUG — only one dynamic-texture task survives per worker tick

**Where** — [`WorkerThread.dynamicTextureQueue`](../common/src/main/java/org/mtr/render/WorkerThread.java) line 31, and `scheduleDynamicTextures` line 86–88.

```java
private final AtomicReference<@Nullable Runnable> dynamicTextureQueue = new AtomicReference<>();
public void scheduleDynamicTextures(Runnable runnable) {
	dynamicTextureQueue.set(runnable);   // overwrites any pending task
}
```

The queue is a **single slot**. If thirty stations all request their dynamic textures in
the same frame (typical when the player turns a corner and a row of PIDS comes into view),
`scheduleDynamicTextures` is called thirty times and only the **last** runnable survives.
The other twenty-nine generation requests are silently dropped on the floor.

What rescues correctness is `DynamicTextureCache.getResource` lines 252–253: the lost keys
are still in `generatingResources`, so the caller keeps getting the
`DefaultRenderingColor` placeholder. They will be re-requested next frame (because the
`needsRefresh` flag is still set on the old `DynamicResource`, or because `dynamicResources`
still has no entry), so eventually each texture gets generated — but it takes **N frames
for N textures**, producing the well-known "textures pop in one at a time" effect when a
large station first appears.

**Fix**

Replace the `AtomicReference` slot with a real bounded queue:

```java
private final java.util.concurrent.ConcurrentLinkedQueue<Runnable> dynamicTextureQueue = new java.util.concurrent.ConcurrentLinkedQueue<>();
```

Drain the queue per worker tick, but cap how many runnables run per tick to keep the worker
responsive to occlusion-culling requests (e.g. up to 4 per 10 ms sleep cycle, configurable
via a new `Config` value next to `dynamicTextureResolution`). With virtual threads, the
"cap" is mostly a soft throttle to avoid flooding the main thread's
`resourceRegistryQueue` (see §2.4).

Apply the same fix to `occlusionQueueVehicle` / `occlusionQueueLift` / `occlusionQueueRail`
(lines 28–30) — they have the identical "single slot overwrites" issue, though the symptoms
are less visible because culling is per-frame and self-healing.

**Risk / scope** — Low. The worker loop already handles `null` tasks via the `run(...)`
helper at lines 102–111.

### 2.2 Font fallback scans every system font, per missing character

**Where** — [`DynamicTextureCache.getTextPixels`](../common/src/main/java/org/mtr/client/DynamicTextureCache.java) lines 188–196.

```java
for (final Font testFont : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
	if (testFont.canDisplay(character)) {
		defaultFont = testFont;
		break;
	}
}
```

`GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()` enumerates every font on
the user's machine — on Windows with a typical office install that's 200–400 fonts, and the
call itself takes 50–200 ms cold. We call it **per character that neither bundled font can
display**, every time a new station name is rendered. For a name like `Lo Wu (羅湖) [LZN]`
with a glyph the CJK font also lacks, this can stall the worker thread for hundreds of ms.

**Fix**

1. Lazy-cache the AWT font list once per JVM (`private static final Font[] SYSTEM_FONTS = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();`) — but make the field
   initialise on the worker thread, not the main thread (use a `Lazy` holder).
2. Better: maintain a `Char2ObjectOpenHashMap<Font>` of "character → font that can display
   it" memoised across calls. The vast majority of glyphs in any single session resolve to
   one of three or four fonts.
3. Best: ship one extra fallback font in the resource pack (e.g. Noto Sans Symbols2) and
   skip the `GraphicsEnvironment` enumeration entirely. The current bundled fonts are
   `noto-sans-semibold.ttf` and `noto-serif-cjk-tc-semibold.ttf`; adding the symbols font
   would cover the common gap (arrows, emoji-adjacent symbols, line markers).

**Risk / scope** — Low.

### 2.3 `getTextPixels` allocates per-character

The inner loop (lines 180–199) calls `font.deriveFont(...)` twice per text part, computes
`getStringBounds` per character (allocates a `Rectangle2D.Double` then a `Rectangle`), and
walks `AttributedString.addAttribute` per character. For a 20-character station name on a
4-row PIDS, this is hundreds of small allocations.

**Fix**

- Hoist `font.deriveFont(...)` / `fontCjk.deriveFont(...)` out of the per-text-part loop —
  they only depend on `newFontSize`, which is constant per text part.
- Replace the per-character bounds with `font.getStringBounds(textSplit[index], context)`
  once, then walk the `GlyphVector` to assign per-glyph fonts. Reduces allocations from
  O(chars) to O(text-parts).
- Reuse a single `FontRenderContext` instance instead of `new FontRenderContext(new AffineTransform(), false, false)` per call.

**Risk / scope** — Low–medium. The glyph-vector path needs care for the CJK-vs-Latin mixed
case.

### 2.4 Main-thread image downscale loop

**Where** — [`DynamicTextureCache.getResource` lines 289–299](../common/src/main/java/org/mtr/client/DynamicTextureCache.java).

When a generated `NativeImage` exceeds `newMaxImageSize`, the code allocates a new image
and copies pixels with:

```java
for (int x = 0; x < ...; x++) {
	for (int y = 0; y < ...; y++) {
		newNativeImage.setColorArgb(x, y, nativeImage.getColorArgb(x, y));
	}
}
```

This runs inside `resourceRegistryQueue` which is drained on the **render thread** (see
`getResource` line 244 and the `process` call at the start of the next frame).
`setColorArgb` is a JNI call into native memory per pixel. A 2048×2048 downscale is 4 M JNI
hops on the main thread = 30–100 ms hitch.

**Fix**

- Do the downscale on the worker thread, **before** putting the runnable onto
  `resourceRegistryQueue`. Only `registerTexture` truly has to be main-thread.
- Replace the per-pixel loop with `NativeImage.copyFrom(other)` or a `MemoryUtil.memCopy`
  on the underlying buffers. If actual *scaling* is intended (which the current code does
  **not** do — it just crops to the top-left N×N), call `nativeImage.resizeSubRectTo(...)` or
  use a 2D mipmap step. **Note the current code is also a bug**: it crops, it doesn't scale
  down. A 4096×512 image becomes a 2048×512 image showing only the left half.

**Risk / scope** — Low for the move-to-worker change; medium for fixing the crop-vs-scale
bug (it's a behaviour change that affects appearance).

### 2.5 Cache key string churn

Every `getXxx(...)` accessor on `DynamicTextureCache` (lines 97–143) builds a key with
`String.format("…_%s_%s_…", …)`. These methods are called from `RenderPIDS`,
`RenderStationNameBase`, etc., **once per sign per frame**. `String.format` is famously slow
— ~1 µs per call, ~6 calls per sign, 100 signs in view = 600 µs/frame just on key
formatting.

**Fix**

- Replace `String.format` with `StringBuilder` concatenation, or with a record-typed key
  (`record TextKey(String text, int color, int maxWidth, double cjkSizeRatio, boolean fullPixel) {}`)
  and switch `dynamicResources` to `Object2ObjectOpenHashMap<Object, DynamicResource>`.
- Record-keyed maps generate identity hash codes from the components without string churn
  and let the JIT inline `equals`.

**Risk / scope** — Low. Mechanical refactor.

### 2.6 `tick()` walks the whole cache every render frame

**Where** — `DynamicTextureCache.tick()` lines 75–95.

Two full-map iterations every frame:

1. `dynamicResources.forEach(...)` — checks `expiryTime < currentTimeMillis`.
2. `deletedResources.forEach(...)` — checks deletion-cooldown expiry.

With several hundred cached textures, that's a few thousand entries scanned per frame even
when nothing is changing.

**Fix**

Order entries by expiry time. `dynamicResources` is already an
`Object2ObjectLinkedOpenHashMap` so insertion order is preserved — but `expiryTime` is
bumped on every access (line 248), so insertion order ≠ expiry order. Switch to a
`PriorityQueue<DynamicResource>` keyed by expiry (re-insert on access) or amortise: only
scan the map every 1 s, not every frame.

`deletedResources` should similarly be a min-heap by expiry.

**Risk / scope** — Low.

### 2.7 `refresh()` invalidates *all* cached textures

**Where** — `DynamicTextureCache.refresh()` lines 69–73.

Currently every reload, every config change, every `reload()` cascade nukes the whole cache
and forces regeneration of every dynamic texture in the world. Where is it called?
`reload()` itself, and any callers should be audited — wholesale invalidation should be
rare. If `refresh()` is being called from font/config setters, prefer per-key
invalidation.

**Action** — Audit callers of `refresh()` (`grep -r "DynamicTextureCache.instance.refresh"`)
and verify each is intentional. Document the contract on the method.

---

## 3. Rendering — frame-rate drops with many entities

The render path is `MainRenderer.render` → `RenderVehicles.render` / `RenderLifts.render` /
`RenderRails.render` → schedule → drain (lines 71–137 of
[`MainRenderer.java`](../common/src/main/java/org/mtr/render/MainRenderer.java)).

### 3.1 `MODEL_RENDERS` cleared and rebuilt every frame, with `computeIfAbsent` chains

**Where** — [`MainRenderer.java:83-84, 105-106, 139-145`](../common/src/main/java/org/mtr/render/MainRenderer.java).

`MODEL_RENDERS` and `MODEL_RENDERS_TRANSLUCENT` are `Object2ObjectOpenHashMap<NewOptimizedModel, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<...>>>`. Every frame:

1. `clear()` on both top-level maps.
2. For each scheduled instance, two `computeIfAbsent` calls create up to two new inner
   maps + one new `ObjectArrayList`.
3. Then `renderModel` walks the maps and drains them.

The `computeIfAbsent` lambdas allocate one map and one list per **first-seen
`<model, stage>` pair** per frame. With ~30 vehicle types, 5 render stages, 200 cars in
view, peak first-frame allocations are in the thousands.

**Fix**

- Reuse the inner maps and lists across frames: instead of `clear()`-ing the top map,
  iterate every entry, `clear()` each inner list, and free only entries whose lists
  remained empty for N frames (LRU pool).
- Even simpler: replace the nested map with a flat `ObjectArrayList<RenderJob>` where
  `RenderJob` is a record `(NewOptimizedModel, RenderStage, StoredMatrixTransformations, int light)`.
  Sort once at the end of `render(...)` by `(model, stage)` for batching, then walk.
  Sorting 1k–10k records is sub-millisecond; the map allocation is not.

**Risk / scope** — Medium. Touches the contract with `RenderVehicles`/`RenderLifts`/`RenderRails`.

### 3.2 Per-instance GPU state changes in `renderModel`

**Where** — [`MainRenderer.renderModel`](../common/src/main/java/org/mtr/render/MainRenderer.java) lines 186–205 and
[`NewOptimizedModel.render`](../common/src/main/java/org/mtr/model/NewOptimizedModel.java) lines 30–42.

For every instance of every model, `NewOptimizedModel.render` does:

```java
shaderProgram.modelViewMat.set(new Matrix4f(RenderSystem.getModelViewMatrix()).mul(matrix4f));
shaderProgram.modelViewMat.upload();
shaderProgram.colorModulator.set(new float[]{lightMultiplier, lightMultiplier, lightMultiplier, 1});
shaderProgram.colorModulator.upload();
vertexBuffer.draw();
```

Two `Matrix4f` allocations per draw call, plus a `float[4]` allocation per draw call, plus
two GL uniform uploads, plus a separate draw. With 200 cars × 4 stages × 6 sub-meshes that
is ~4800 draw calls, ~10k `Matrix4f` allocations, and ~10k uniform uploads per frame. On
laptop iGPUs this is the dominant cost.

**Fix**

1. **Cache the float[4]** — `colorModulator` only depends on `lightMultiplier` and we have
   16 possible values (light 0–15 + always-bright). Pre-build 17 static
   `float[4]` instances and pass by reference.
2. **Reuse a single `Matrix4f` scratch** — `private static final Matrix4f SCRATCH = new Matrix4f();`
   reset and `.mul` into it. The current code constructs a defensive copy because the
   shader uniform "borrows" the reference; the fix is to call
   `modelViewMat.set(SCRATCH)` which already does a defensive copy internally
   (or to fence the scratch with a clear before next use).
3. **Instanced rendering** — for repeated identical models (rails, identical PSD doors,
   identical seats), upload the per-instance matrices to a UBO/SSBO and issue a single
   `glDrawElementsInstanced`. This is the largest possible win but the most invasive change
   (touches `NewOptimizedModel`, `VertexBuffer`, and the shader).
4. **Sort by texture/shader before drawing** — `MODEL_RENDERS` is keyed by
   `NewOptimizedModel` which already groups by texture, but the two-stage emission
   (opaque pass then translucent pass) re-binds shaders for each model. Sort instead by
   `(shader, texture, model)` to minimise rebinds.

**Risk / scope** — Items 1 and 2 are trivial. Item 4 is medium. Item 3 is large but pays for itself ~10×.

### 3.3 `QueuedRenderLayer.values()` allocated per frame

**Where** — [`MainRenderer.render`](../common/src/main/java/org/mtr/render/MainRenderer.java) lines 109–117.

```java
for (int j = 0; j < QueuedRenderLayer.values().length; j++) {
	...
	final QueuedRenderLayer queuedRenderLayer = QueuedRenderLayer.values()[j];
```

`Enum.values()` allocates a new array on every call. Called 2 × N_layers + 1 times per
frame, that is ~20 array allocations per frame for nothing.

**Fix**

Add `private static final QueuedRenderLayer[] QUEUED_RENDER_LAYERS = QueuedRenderLayer.values();`
once, then use the cached array. Same pattern applies to `RenderStage.values()` and
any other enum used in render loops.

**Risk / scope** — Trivial.

### 3.4 `cancelRender` is O(stages × priorities × map.size)

**Where** — [`MainRenderer.cancelRender`](../common/src/main/java/org/mtr/render/MainRenderer.java) lines 157–160.

```java
RENDERS.forEach(renderForPriority -> renderForPriority.forEach(renderForPriorityAndQueuedRenderLayer -> renderForPriorityAndQueuedRenderLayer.remove(identifier)));
CURRENT_RENDERS.forEach(...);
```

This is called from `DynamicTextureCache.DynamicResource.remove()` which fires whenever a
dynamic texture is evicted. During large `refresh()` waves (resource reload), this can be
called for hundreds of identifiers in one frame; each invocation walks the full nested
structure. The maps are tiny so the constant factor is small, but it's still wasted work.

**Fix**

Batch the cancellations: `cancelRenders(Collection<Identifier>)` that walks the structure
once and removes all identifiers in one pass.

**Risk / scope** — Low.

### 3.5 Heavy `Collectors.toCollection` chains inside per-vehicle loops

**Where** — [`RenderVehicles.render`](../common/src/main/java/org/mtr/render/RenderVehicles.java) lines 66–75.

```java
final ObjectArrayList<...> vehiclePropertiesList = vehicle.getSmoothedVehicleCarsAndPositions(millisElapsed)
	.stream()
	.map(vehicleCarAndPosition -> {
		final ObjectArrayList<PositionAndRotation> bogiePositions = vehicleCarAndPosition.right()
			.stream()
			.map(...)
			.collect(Collectors.toCollection(ObjectArrayList::new));
		return new ObjectObjectImmutablePair<>(...);
	})
	.collect(Collectors.toCollection(ObjectArrayList::new));
```

Per vehicle, this allocates one outer stream + one inner stream **per car** + a `Pair` per
car. For a 12-car train that's ~25 stream pipelines plus ~25 list allocations, every frame,
per vehicle, even before any rendering happens. With 50 vehicles visible we're at ~1250
streams/frame.

**Fix**

Replace with imperative loops over fastutil lists. Pre-allocate `vehiclePropertiesList`
once (size known from `vehicle.getSmoothedVehicleCarsAndPositions(...)`); reuse it across
vehicles by clearing between iterations.

**Risk / scope** — Low. Mechanical.

### 3.6 `getInterchangeRouteNames` allocates per call

**Where** — [`MainRenderer.getInterchangeRouteNames`](../common/src/main/java/org/mtr/render/MainRenderer.java) lines 162–166.

`new ObjectArrayList<>()` plus `IGui.mergeStationsWithCommas` (which itself iterates +
concatenates). Called per station-name sign per frame.

**Fix**

Memoise on `(stationId, languageDisplay)` — the result only changes when interchange data
or language setting changes, both rare. Invalidate from the same hook that calls
`DynamicTextureCache.instance.refresh()`.

### 3.7 Rendering pipeline does not skip work for off-screen / far-away vehicles early enough

The occlusion-culling integration runs on the worker thread (see
[`WorkerThread.scheduleVehicles`](../common/src/main/java/org/mtr/render/WorkerThread.java)
lines 74–84), but the main thread in `RenderVehicles.render` still walks **every** vehicle
to compute smoothed positions and stream lists before checking
`CullingHelper.getDistanceFromCamera` (line 99). Move the distance check to be the very
first thing inside the per-vehicle loop, gating everything else.

### 3.8 `MoreRenderLayers` cache lookups in tight loops

`MoreRenderLayers.getLight(...)`, `.getExterior(...)`, etc. are called inside the
draw-loop dispatch (lines 119–132 of `MainRenderer`, and again inside `renderModel` at
lines 189–195). These methods cache by `Identifier`, but each call still does a map lookup.
For 200 cars × 5 render stages this is 1000 map probes per frame.

**Fix** — When the renderer is sorted by `(model, stage)` as proposed in §3.1, the
`RenderLayer` only needs to be looked up once per `(texture, stage)` group, not once per
instance. Hoist the lookup out of the inner loop.

### 3.9 `Object2ObjectOpenHashMap` keyed by `NewOptimizedModel` uses reference identity

`NewOptimizedModel` does not override `hashCode`/`equals`, so the maps in `MainRenderer`
hash by identity. This is correct *as long as* `NewOptimizedModel` instances are
singletons per (model, render-stage) pair. Verify this is true after a `/reload` — if
models are reloaded but old `NewOptimizedModel` instances are still referenced in queued
render jobs (between frames), the renderer will try to draw with a disposed
`VertexBuffer` and silently no-op (lines 31–32 of `NewOptimizedModel.render`). At minimum,
add an `assert` and a debug log when `vertexBuffer == null` to surface dangling refs.

---

## 4. Miscellaneous findings

### 4.1 `MTR.LOGGER.error("", e)` swallows the exception message — §3.14 violation

Found in 20 sites; see the inventory in
[`docs/PENDING.md`](PENDING.md#known-follow-ups). Each one should be replaced with a
descriptive message identifying the operation that failed. The eight sites in
`CustomResourceLoader.java`, `DynamicTextureCache.java`, `ObjModelLoader.java`,
`ResourceManagerHelper.java`, and `RouteMapGenerator.java` have already been fixed as part
of writing this document; the rest are tracked in `PENDING.md`.

### 4.2 Worker thread sleeps 10 ms even when work is pending

**Where** — [`WorkerThread.start`](../common/src/main/java/org/mtr/render/WorkerThread.java) lines 35–58.

The worker loop sleeps 10 ms unconditionally between iterations, even after a long task
just completed. This bounds dynamic-texture throughput to 100 tasks/s regardless of how
many are queued. Replace the unconditional sleep with `wait()` / `notify()` (or
`LockSupport.park`/`unpark` from `scheduleDynamicTextures`) so the worker only sleeps when
genuinely idle.

### 4.3 `Identifier.of("")` and `Identifier.of(MTR.MOD_ID, "id_" + MTR.randomString())` allocations

**Where** — [`MainRenderer.scheduleRender`](../common/src/main/java/org/mtr/render/MainRenderer.java) line 154, and [`DynamicTextureCache.getResource`](../common/src/main/java/org/mtr/client/DynamicTextureCache.java) line 302.

`Identifier.of("")` creates a new `Identifier` per call from
`MainRenderer.scheduleRender(QueuedRenderLayer, ScheduledRender)`. Hoist to
`private static final Identifier EMPTY_IDENTIFIER = Identifier.of("");`.

`Identifier.of(MTR.MOD_ID, "id_" + MTR.randomString())` is fine as a one-off per generated
texture, but `MTR.randomString()` should be checked — if it's `UUID.randomUUID().toString()`
that's a 36-char allocation plus secure-RNG draw per texture, which adds up during a
texture-burst.

### 4.4 `DynamicTextureCache.getResource` calls `RouteMapGenerator.setConstants()` per request

Line 311. `setConstants()` recomputes four `int` values from the config (lines 41–47 of
`RouteMapGenerator.java`). Two of them use `Math.pow(2, …)`. Should be invalidated only
when the config value actually changes, not on every texture request.

### 4.5 `CustomResourceLoader.getRails`/`.getObjects`/`.getLifts`/`.getMinecraftModelResources`/`.getTextureResources`

Each returns `new ObjectImmutableList<>(...)` or `new ObjectArrayList<>(...)` (lines
237–276). These are called from UI listing code; cache the immutable snapshot and
invalidate it from `reload()` rather than rebuilding per call.

### 4.6 `Object2ObjectAVLTreeMap` used as the primary cache type

`AVLTreeMap` is O(log n) per op and chases pointers; `Object2ObjectOpenHashMap` is O(1) for
the same operations and packs entries cache-friendly. Per §3.8 of `CODE_STYLES.md`,
fastutil is already the in-house choice — `AVLTreeMap` is only justified when ordered
iteration is needed. Most uses in `CustomResourceLoader` are point lookups; switch them to
`OpenHashMap`. (Keep `AVLTreeMap` for the sorted display lists in the UI.)

### 4.7 `MinecraftClientData.getInstance().vehicles.forEach(...)` called many times per frame

`MainRenderer.render` walks `vehicles` once for simulation and once via `RenderVehicles.render`
(which walks it again). Pass the list down instead of re-fetching it.

---

## 5. Suggested execution order

1. **Quick wins** (≤ 1 day each, low risk):
   - §3.3 cache `enum.values()` arrays.
   - §3.2 reuse `Matrix4f` scratch and pre-built `float[4]` arrays for light multipliers.
   - §2.5 replace `String.format` keys with `StringBuilder` (or records).
   - §4.3 hoist `Identifier.of("")` to a constant.
   - §4.4 stop calling `RouteMapGenerator.setConstants()` per texture request.
   - §4.2 replace `Thread.sleep(10)` with `wait/notify`.
2. **Medium fixes** (1–3 days each):
   - §2.1 fix the dropped-runnable bug in `WorkerThread.dynamicTextureQueue`.
   - §2.2 cache the AWT system-font enumeration.
   - §2.4 move the downscale loop off the render thread + fix the crop-vs-scale bug.
   - §2.6 amortise `DynamicTextureCache.tick()`.
   - §3.1 + §3.8 flatten `MODEL_RENDERS` to a sortable list, hoist render-layer lookup.
   - §3.5 replace stream chains in `RenderVehicles.render` with imperative loops.
   - §1.2 move OBJ/bbmodel parsing fully onto the worker thread.
3. **Larger refactors** (3+ days, design discussion needed):
   - §1.1 + §1.4 redesign resource caching to stream `InputStream`s and bound memory.
   - §1.3 lazy `VehicleModel` parsing.
   - §3.2 item 3 — instanced rendering for repeated meshes.

Numbers throughout this document are order-of-magnitude estimates derived from reading,
not profiling. Profile each item with a JFR recording on a populated world before and
after each change to confirm the gain.
