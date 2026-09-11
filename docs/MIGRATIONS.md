# In-flight migrations and tech-debt hotspots

> This file catalogues the places where the codebase is visibly mid-refactor — two systems
> running side by side, one "new" version next to its predecessor, or temporary scaffolding
> waiting to be torn down. The goal is to make these visible so they don't get forgotten,
> and so contributors don't accidentally extend the **deprecated** side of each split when
> adding features.
>
> For every migration we record:
> - **What's old / what's new** — the two systems in play.
> - **State** — what % of call sites have moved, what's left.
> - **Finish line** — the concrete state where this entry can be deleted.
> - **Pitfall** — what to **not** do while the migration is in progress.

---

## 1. MTR 3.x → MTR 4.x resource format (the `org.mtr.legacy` package)

**What's old / what's new**

- **Old**: monolithic `mtr_custom_resources.json` files with `custom_trains` / `custom_signs`
  top-level keys, plus per-namespace `rails/` and `eyecandies/` directories from the
  legacy `mtrsteamloco` ecosystem.
- **New**: the schema-generated format under `org.mtr.generated.resource.*`, authored from
  `buildSrc/src/main/resources/schema/resource/*.json`. Each resource type
  (`VehicleResource`, `SignResource`, `RailResource`, `ObjectResource`, `LiftResource`)
  has its own schema and lives in `org.mtr.resource`.

**State**

The runtime always pays the migration cost: every load goes through
[`CustomResourcesConverter.convert(...)`](../common/src/main/java/org/mtr/legacy/resource/CustomResourcesConverter.java)
in [`CustomResourceLoader.reload()`](../common/src/main/java/org/mtr/client/CustomResourceLoader.java)
even for already-new-format packs, because the converter is also the path that recognises
the new format and short-circuits (see `convert`, line 22–24 — it returns immediately if
neither `custom_trains` nor `custom_signs` is present).

The `org.mtr.legacy.*` package is **not** dead code. It is the live import path for:

- `mtrsteamloco`-namespaced rail JSON files (`MOD_ID_NTE` constant in `CustomResourcesConverter`).
- `mtrsteamloco`-namespaced eyecandy JSON files.
- Any pack still shipping the MTR 3.x `custom_trains` / `custom_signs` block.

The converter classes (`LegacyVehicleResource`, `LegacySignResource`, `LegacyRailResource`,
`LegacyObjectResource`) extend the **legacy** schemas under
`org.mtr.legacy.generated.resource.*` and emit new-schema JSON via a `convert(...)` method.

**Finish line**

- A future major version that drops MTR 3.x compatibility. At that point:
  1. Delete `org.mtr.legacy.resource` and `org.mtr.legacy.generated`.
  2. Replace `CustomResourcesConverter.convert(...)` calls in `CustomResourceLoader` with
     direct `new CustomResources(new JsonReader(jsonObject), resourceProvider)`.
  3. Drop the `convertRails(...)` / `convertObjects(...)` helpers — operators must use the
     new-format directories.

**Pitfall**

Do **not** add new fields to the legacy schemas. The legacy schema files exist solely to
parse historical JSON; new fields belong on the new schema in
`buildSrc/src/main/resources/schema/resource/`. Adding a field to a legacy schema doesn't
do anything useful because the legacy-to-new conversion is a one-shot translation, not a
runtime fallback.

## 2. `mtr_custom_resources_pending_migration.json`

**What's old / what's new**

There is a second JSON-load step inside `CustomResourceLoader.reload()` (lines 113–120):

```java
// TODO temporary code for loading models pending migration
ResourceManagerHelper.readAllResources(Identifier.of(MTR.MOD_ID, CUSTOM_RESOURCES_PENDING_MIGRATION_ID + ".json"), ...);
```

`CUSTOM_RESOURCES_PENDING_MIGRATION_ID = "mtr_custom_resources_pending_migration"`. This is
a parallel resource manifest with the **same** legacy schema as `mtr_custom_resources.json`,
loaded immediately after it. It exists so that built-in vehicles being moved from
hand-authored configs to schema-authored configs can ship in two halves: the already-converted
half in the main manifest, and the half still pending conversion in the migration manifest.

**State**

- Living in `common/src/main/java/org/mtr/mtr_custom_resources_template.json` and
  `common/src/main/java/org/mtr/vehicle_templates/`.
- The dual load is **not** a fallback — both files are read and both contribute vehicles.
  If the same vehicle id is in both, it is registered twice and the duplicate-id warning
  fires in `validateDataset`.

**Finish line**

When the last entry has moved out of the pending-migration manifest:
1. Delete `mtr_custom_resources_pending_migration.json` from the resource pack.
2. Delete the `CUSTOM_RESOURCES_PENDING_MIGRATION_ID` constant.
3. Delete the second `readAllResources(...)` block in `reload()`.
4. Delete the `// TODO temporary code for loading models pending migration` comment.

**Pitfall**

When adding a brand-new vehicle, put it directly in the new-format manifest. The pending
manifest is for migration-in-progress entries only — do not use it as a place to land new
content.

## 3. Two `DoorAnimationType` enums

**What's old / what's new**

- **Old**: [`org.mtr.client.DoorAnimationType`](../common/src/main/java/org/mtr/client/DoorAnimationType.java)
  — `float`-based, parameterised on the enum constructor with a `maxTime` field, exposes
  static helpers `getDoorAnimationX(type, value)` and
  `getDoorAnimationZ(type, doorMax, duration, value, opening)`.
- **New**: [`org.mtr.resource.DoorAnimationType`](../common/src/main/java/org/mtr/resource/DoorAnimationType.java)
  — `double`-based, instance methods `getDoorAnimationX(multiplier, flipped, time)` and
  `getDoorAnimationZ(multiplier, flipped, time, opening)`. Adds `PLUG_SLOW_2` and
  `PLUG_SLOW_3` variants that the legacy enum does not have.

Both enums share constant names (`STANDARD`, `STANDARD_SLOW`, `CONSTANT`, `PLUG_FAST`,
`PLUG_SLOW`, `BOUNCY_1`, `BOUNCY_2`, `MLR`, `R179`, `R211`). The body of every shared case
is structurally identical between the two files — the only real differences are:

1. Precision (`float` vs `double`).
2. The "duration" parameter has been baked in to 0.5 s as a constant in the new enum.
3. The new enum hangs the dispatch off the instance (`switch (this)`) rather than a static
   `switch (doorAnimationType)`.
4. Sign / flip handling is now generic via `Math.copySign(..., (flipped ? -1 : 1) * multiplier)`
   instead of being open-coded at each call site.

**State**

- Anything that touches `org.mtr.resource.ModelPropertiesPart`,
  `org.mtr.resource.ModelPropertiesPartWrapper`, or the schema-generated
  `ModelPropertiesPartSchema` is on the **new** enum.
- The **old** enum (`org.mtr.client.DoorAnimationType`) appears to no longer be referenced
  outside its own file. Validate with
  `grep -r "org.mtr.client.DoorAnimationType" common/` before deletion — if zero hits, the
  file is safe to delete in the same change that updates `CODE_STYLES.md` to remove the
  "duplicate enums are tolerated" precedent.

**Finish line**

Delete `common/src/main/java/org/mtr/client/DoorAnimationType.java` once nothing imports it.

**Pitfall**

Do not import `org.mtr.client.DoorAnimationType` in new code, even though IntelliJ will
auto-suggest both. Always use the `org.mtr.resource` one.

## 4. `NewOptimizedModel` — the "New" is a renaming leftover

**What's old / what's new**

The "New" prefix is from a previous mesh-building rewrite. There is no `OptimizedModel`
(without the `New` prefix) anywhere in the tree any more, but the prefix has been
preserved across:

- [`NewOptimizedModel`](../common/src/main/java/org/mtr/model/NewOptimizedModel.java)
- [`NewOptimizedModelGroup`](../common/src/main/java/org/mtr/model/NewOptimizedModelGroup.java)
- Every field that holds one (`builtModel2`, `nameToNewOptimizedModelGroup`, …).

**State**

Pure naming debt. The "new" implementation is the only implementation.

**Finish line**

Rename `NewOptimizedModel` → `OptimizedModel` and
`NewOptimizedModelGroup` → `OptimizedModelGroup` across `common/`. The rename is mechanical
because the old name does not exist as a separate type; an IntelliJ "Rename" refactor
handles it.

**Pitfall**

When the rename happens, **also** update the comment in
[`VehicleResource.vehicleResourceCacheInitializer`](../common/src/main/java/org/mtr/resource/VehicleResource.java)
line 320 (`// TODO don't rebuild shared models, e.g. bogies`) — that TODO is wired to the
old mental model where rebuilds were cheap. With the renamed type plus deferred parsing
(see `PERFORMANCE.md` §1.3), bogie-sharing becomes feasible.

## 5. `ModelLoaderBase` — two parallel "build" paths

**Where** — [`common/src/main/java/org/mtr/model/ModelLoaderBase.java`](../common/src/main/java/org/mtr/model/ModelLoaderBase.java).

**What's old / what's new**

`ModelLoaderBase` exposes **two** memoised-build methods:

```java
public final BuiltVehicleModelHolder get(ModelProperties modelProperties, PositionDefinitions positionDefinitions) { … } // line 42, stores into builtModel1
public final Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> get() { … }                       // line 61, stores into builtModel2
```

`get(modelProperties, positionDefinitions)` is the **vehicle** path — it knows about
floors, doorways, door mappings, conditional parts, and display surfaces.
`get()` is the **non-vehicle** path — used by `ObjectResource`, `RailResource`, and the
defaults helper.

The two paths have **different caches** (`builtModel1` vs `builtModel2`) and **different
return types**, so the same `ModelLoaderBase` instance can answer either question — but in
practice each instance is created for one of the two callers and the other accessor is
never called. That's invisible from the type system, which makes "is `get()` safe to call
on a `ModelLoaderBase` produced for a vehicle?" a question whose answer is "it returns a
flattened-into-`EXTERIOR` blob that doesn't match anything the vehicle renderer expects."

**State**

This is **deferred consolidation**, not active migration — both paths are needed. But the
shared-base-class structure is confusing: a future refactor should split
`ModelLoaderBase` into:

- `VehicleModelLoader` interface returning `BuiltVehicleModelHolder`.
- `SimpleModelLoader` interface returning the render-stage map.
- A common `AbstractModelLoader` that owns the parsed `nameToNewOptimizedModelGroup` map.

**Pitfall**

Do **not** rely on `get()` returning a vehicle's parts in a usable form. If you find
yourself wanting to call `get()` on a `VehicleModel`'s loader, you almost certainly want
`get(modelProperties, positionDefinitions)` instead.

## 6. Model-format dispatch is duplicated between `VehicleModel` and `StoredModelResourceBase`

**Where**
- [`VehicleModel.getModelLoaderBase`](../common/src/main/java/org/mtr/resource/VehicleModel.java) lines 133–159.
- [`StoredModelResourceBase.load`](../common/src/main/java/org/mtr/resource/StoredModelResourceBase.java) lines 20–45.

Both branches do the same `.bbmodel` / `.obj` / fallback dispatch, call
`new BlockbenchModelLoader(...)` or `new ObjModelLoader(...)`, and route the same texture
resolvers in. Both carry the same `// TODO transform object if needed` comment (line 38 of
`StoredModelResourceBase`, line 149 of `VehicleModel`). The duplication is the reason
those two TODOs are stuck — fixing one requires touching the other.

**Finish line**

Extract a single static factory:

```java
package org.mtr.resource;

public final class ModelLoaderFactory {
	public static ModelLoaderBase create(String modelResource, String textureResource, ResourceProvider resourceProvider, boolean flipTextureV) { … }
}
```

…and route both call sites through it. Resolve the `// TODO transform object if needed`
once at that single site (the open question is whether OBJ vertices need a coordinate-system
flip; the answer is the same wherever the OBJ comes from).

**Pitfall**

If you need to add a new model format (e.g. `.gltf`), do **not** add it in both call sites
separately. Extract the factory **first**, then add the new branch in one place.

## 7. `StoredModelResourceBase.preload()` is a no-op marked `// TODO`

**Where** — [`common/src/main/java/org/mtr/resource/StoredModelResourceBase.java`](../common/src/main/java/org/mtr/resource/StoredModelResourceBase.java) lines 57–59.

The interface method is called from
[`CustomResourceLoader.reload()`](../common/src/main/java/org/mtr/client/CustomResourceLoader.java)
lines 162–186 for every rail and every object whose id matches the
preload-resource-pattern. The current body is `// TODO` — i.e. **preloading rails and
objects does nothing**. Only vehicles actually preload.

This is wired to the broader resource-loading refactor in
[`PERFORMANCE.md` §1.3 / §1.6](PERFORMANCE.md). The right implementation is to call into
the eventual lazy `ModelLoaderBase.get()` synchronously to warm the cache.

**Pitfall**

Do not assume `shouldPreload` actually preloads for non-vehicles. If a rail texture
hitches on first encounter despite `shouldPreload = true`, this no-op is why.

## 8. Two vehicle sound systems coexist

**What's old / what's new**

- **Old**: [`LegacyVehicleSound`](../common/src/main/java/org/mtr/sound/LegacyVehicleSound.java) — six
  `legacy*` configuration fields (`legacySpeedSoundBaseResource`,
  `legacySpeedSoundCount`, `legacyUseAccelerationSoundsWhenCoasting`,
  `legacyConstantPlaybackSpeed`, `legacyDoorSoundBaseResource`,
  `legacyDoorCloseSoundTime`).
- **New**: [`BveVehicleSound`](../common/src/main/java/org/mtr/sound/BveVehicleSound.java) +
  `BveVehicleSoundConfig` — BVE-style sound config (per-throttle-step samples, named
  channels, etc.).

Selection happens in
[`VehicleResource.createVehicleSoundBaseInitializer`](../common/src/main/java/org/mtr/resource/VehicleResource.java)
lines 357–372: if `bveSoundBaseResource.isEmpty()`, the legacy sound is used; otherwise the
BVE sound is used. There's no fallback path — they are mutually exclusive per-vehicle.

The `legacy*` fields above are **not** in `org.mtr.legacy.*` — they are first-class fields
on the **new** schema (`org.mtr.generated.resource.VehicleResourceSchema`). The naming is
honest: a new-format vehicle can still describe MTR 3.x-style sounds. So this is not a
migration to be finished by deleting the legacy code; it's two valid configurations of the
same vehicle.

**Pitfall**

Do not delete `LegacyVehicleSound`. The MTR 3.x sound system is a supported authoring
choice, not deprecated. Renaming the fields away from the `legacy*` prefix is reasonable
(e.g. `simpleSpeedSoundBaseResource`); deleting the class is not.

Also covered by [`BveVehicleSound.java:121` — `// TODO play flange sounds`](../common/src/main/java/org/mtr/sound/BveVehicleSound.java): the new system is itself incomplete.

## 9. `VehicleResource.extraModelsSupplier` (`LegacyVehicleSupplier`)

**Where** — [`common/src/main/java/org/mtr/resource/VehicleResource.java`](../common/src/main/java/org/mtr/resource/VehicleResource.java) lines 34, 374–377.

A `@FunctionalInterface` named `LegacyVehicleSupplier<T>` with signature
`T apply(int carNumber, int totalCars)` — supplies "extra" models that depend on a car's
position in a train (front cab vs trailer vs back cab). It is **nullable** because the new
multi-model schema (`models` / `bogie1Models` / `bogie2Models` fields) can express
position-dependent cars directly through the schema; the legacy MTR-3.x format could not.

The two-path dispatch is at lines 134–135 and 273–278: if `extraModelsSupplier == null`,
ignore `carNumber` / `totalCars`; otherwise dispatch into the legacy supplier.

**Finish line**

Tied to migration #1. When `org.mtr.legacy.*` goes away, every `VehicleResource` will be
constructed without the supplier, the `extraModelsSupplier` field can be deleted, and
`getCachedVehicleResource(carNumber, totalCars)` can drop its newCarNumber/newTotalCars
collapse logic.

**Pitfall**

Adding new model fields to the new schema is fine; adding them by extending
`LegacyVehicleSupplier` is not.

## 10. Two list/widget systems: `ScrollableListWidget` and `ListComponent`

**Where**
- [`org.mtr.widget.ScrollableListWidget`](../common/src/main/java/org/mtr/widget/ScrollableListWidget.java) — the older Minecraft-screen-widget-based list (extends `EntryListWidget`).
- [`org.mtr.widget.ListComponent`](../common/src/main/java/org/mtr/widget/ListComponent.java) — the newer composable component used inside the resource-pack-creator preview UI.

Both classes have the same `// TODO use vehicle family instead` comment at
`ScrollableListWidget.java:246` and `ListComponent.java:223`. The duplicated TODO is the
fingerprint — the "vehicle family" concept needs to be implemented once and consumed by
both. Until then, each list has its own ad-hoc family detection inline.

**Finish line**

Implement a `VehicleFamily` resolver in `org.mtr.tool` (or similar). Replace both inline
TODO sites with the resolver call. After that, decide whether
`ScrollableListWidget` is still needed at all — the resource-pack-creator UI has been
moving toward `ListComponent`.

**Pitfall**

If you add a new vehicle-list view, prefer `ListComponent`. Touching
`ScrollableListWidget` should be reserved for bug fixes to the older flow.

## 11. Misc TODOs that look like deferred sub-migrations

Smaller, more localised, but worth tracking together. The list is exhaustive across
`common/src/main/java/org/mtr/` as of this writing:

| File:line | Comment | What it suggests |
|---|---|---|
| [`block/BlockPIDSHorizontalBase.java:44`](../common/src/main/java/org/mtr/block/BlockPIDSHorizontalBase.java) | `// TODO copy NBT when copying block` | NBT-copy hook missing on a block that ships data in BE. |
| [`block/BlockPSDAPGDoorBase.java:84, 103`](../common/src/main/java/org/mtr/block/BlockPSDAPGDoorBase.java) | `// TODO don't hard code these bounds` / bare `// TODO` | Hard-coded hitbox extents that should come from `ModelProperties`. |
| [`data/VehicleExtension.java:127`](../common/src/main/java/org/mtr/data/VehicleExtension.java) | `// TODO chat announcements (next station, route number, etc.)` | In-train announcements not implemented yet. |
| [`packet/PacketDriveTrain.java:33`](../common/src/main/java/org/mtr/packet/PacketDriveTrain.java) | bare `// TODO` | Inline `// TODO` next to driver-input handling — context-light, needs investigation. |
| [`packet/PacketUpdateLiftTrackFloorConfig.java:48`](../common/src/main/java/org/mtr/packet/PacketUpdateLiftTrackFloorConfig.java) | `// TODO update lift floor` | Floor-update path not wired through. |
| [`resource/ModelPropertiesPart.java:163`](../common/src/main/java/org/mtr/resource/ModelPropertiesPart.java) | `// TODO figure out why inconsistent translations are needed` | A "do not change this magic offset" sign — needs an actual investigation. |
| [`resource/VehicleResource.java:320`](../common/src/main/java/org/mtr/resource/VehicleResource.java) | `// TODO don't rebuild shared models, e.g. bogies` | Performance — see `PERFORMANCE.md`. |
| [`servlet/ResourcePackCreatorOperationServlet.java:47, 111`](../common/src/main/java/org/mtr/servlet/ResourcePackCreatorOperationServlet.java) | `// TODO load backup` / `// TODO save backup` | Backup/restore for the resource-pack-creator is stubbed. |
| [`sound/BveVehicleSound.java:121`](../common/src/main/java/org/mtr/sound/BveVehicleSound.java) | `// TODO play flange sounds` | Migration #8 — BVE sounds incomplete. |
| [`widget/PreviewBoxComponent.java:131`](../common/src/main/java/org/mtr/widget/PreviewBoxComponent.java) | `// TODO figure out why is this Z offset needed?` | Mystery Z-offset — investigation needed. |

Each row above should either:
1. Be resolved (preferred when the fix is bounded).
2. Or be promoted to its own entry in this document or in `PENDING.md`, with the level of
   detail the rest of this file uses.

A `// TODO` without follow-up over a year old is a bug. Audit this list each release.

---

## 12. Minecraft 26.1 port

**What's old / what's new**

- **Old**: 1.21.1 and 1.21.4, obfuscated, built through Loom's remapping variant.
- **New**: 26.1.2, unobfuscated, built through Loom's non-remapping variant. Minecraft has
  used year-based versions since 2026, and everything from 26.1 onwards ships without
  obfuscation, so mappings no longer exist and Yarn is discontinued.

**State**

All six nodes compile. On NeoForge 26.1.2 the whole core loop has been run and confirmed, on the
client and on a dedicated server: laying every type of rail, building tunnels, tunnel walls and
bridges with their material selection, the Rail Dashboard including its world map across a
variety of biomes, creating stations, depots and routes, recalculating them, choosing vehicles
and cars in a depot, and trains generating and running a route with their models rendering
correctly. The web server starts and serves the system map.

Fabric 26.1.2 has been played in singleplayer since 2026-09-11 with the same railway: the world
loads, rails and the dashboard work, and trains run their routes. Both loaders have also been
run as dedicated servers with a copy of that world: the mod registers, the data pack loads with
every recipe, the railway data is read and written back, and `stop` shuts down cleanly.

Not yet exercised at runtime:
- Block entity data on 1.21.4-format worlds is confirmed: the baseline world's three PIDS with
  populated `platform_ids` loaded, displayed their platforms, and were written back by 26.1.2 as
  `LongArray` with identical values; `LastUpdate` on those chunks moved, so the write went
  through the mod's own save path rather than the upgrade's NBT copy. Data first written by
  26.1.2 in a fresh world has been through many reloads in play.
- Boats, cable cars, planes, lifts, and signalling beyond what a single line exercises.

Known to be broken:

- Two text sites still draw into the world buffer rather than the screen, so they are invisible:
  the platform number badge on list rows (`ScrollableListWidget.drawPlatformNumber` and the same
  method in `ListComponent`) and the warning marker in `VehicleSelectorScreen.drawVehicleIcon`.
  Both are reached through `ListItem.DeferredDrawIcon`, which carries a `PoseStack` but no
  `GuiGraphics`, so fixing them means threading the screen through that interface.

Two techniques are in use, and the choice between them is deliberate:

- **Rewritten while building**, declared in `stonecutter.gradle.kts` under
  `replacements`. Used only where a name changed and behaviour did not, such as
  `ResourceLocation` becoming `Identifier`, the packages that moved, and the
  `EventBusSubscriber` attribute that was deleted. This keeps roughly 220 sites free of
  guards and leaves the shared source untouched.
- **Guarded in the source** with `//? if >= 26.1 {`. Used where behaviour differs, such as
  the NBT getters that now return `Optional`. A reader of those methods needs to see that
  two forms exist, which a rewrite would hide.

What remains, largest first:

| Item | Needs a client? | Notes |
|---|---|---|
| The two `DeferredDrawIcon` text sites | yes | Listed under *State* above |
| Everything past a single train line | yes | Boats, cable cars, planes, lifts, wider signalling |

The "needs a client" column is the important one. Everything marked no can be finished against
the compiler. Everything marked yes compiles just as happily when it is wrong, and shows up only
as incorrect drawing, wrong draw order, or a collapsed frame rate, so it wants someone watching
the game rather than the build log. Every rendering defect found so far was of that kind.

The `GuiGraphics` work is the substantial one. Minecraft 26.1 replaced immediate-mode
drawing with retained-mode extraction: `Screen.render(GuiGraphics, ...)` became
`Screen.extractRenderState(GuiGraphicsExtractor, ...)`. The drawing vocabulary largely
survives, but `blit` and `fill` take a `RenderPipeline` first, `drawString` became `text`
and `centeredText`, and `pose()` returns a two-dimensional `Matrix3x2fStack` rather than a
`PoseStack`. That last one is the only part needing real thought.

**Fabric API replacements**

Verified against Fabric API 0.155.2+26.1.2 by reading the shipped module jars, since several
of these modules were deleted outright rather than renamed:

| Old | New |
|---|---|
| `client.rendering.v1.ColorProviderRegistry` | `client.rendering.v1.BlockColorRegistry` |
| `client.rendering.v1.HudRenderCallback` | `client.rendering.v1.hud.HudElementRegistry` |
| `client.rendering.v1.HudLayerRegistrationCallback` | `client.rendering.v1.hud.HudElementRegistry` |
| `client.rendering.v1.IdentifiedLayer` | `client.rendering.v1.hud.VanillaHudElements` |
| `client.rendering.v1.WorldRenderEvents` | `client.rendering.v1.level.LevelRenderEvents` |
| `client.keybinding.v1.KeyBindingHelper` | `client.keymapping.v1.KeyMappingHelper` |
| `itemgroup.v1.FabricItemGroup` | `creativetab.v1.FabricCreativeModeTab` |
| `blockrenderlayer.v1.BlockRenderLayerMap` | **no replacement** — see below |

Two of these are not mechanical and want a running client before they are settled.

`WorldRenderEvents.AFTER_ENTITIES` has no exact counterpart. `LevelRenderEvents` splits the
frame far more finely, into `AFTER_OPAQUE_TERRAIN`, `AFTER_SOLID_FEATURES`,
`BEFORE_TRANSLUCENT_TERRAIN`, `AFTER_TRANSLUCENT_FEATURES`, `COLLECT_SUBMITS` and others.
`AFTER_SOLID_FEATURES` is the closest reading of the old behaviour, but this is where every
vehicle, rail and sign is drawn, so the wrong choice changes draw order and depth sorting
rather than failing to compile.

`BlockRenderLayerMap` is gone with nothing to replace it. Assigning a render layer to a
block moved out of code and into the block model JSON, so the fix is a resource change
across the affected models rather than a source change. `ChunkSectionLayerHelper` is not a
substitute; it only converts between layer and render type.

**Block tooltips are gone on 26.1**

Minecraft keeps `appendHoverText` on `Item`, adding a display parameter and swapping the list
for a consumer, and removes it from `Block` entirely. Nothing in the game, NeoForge or Fabric
offers a block-side replacement, so the fifteen block classes that had tooltips now compile
that method only below 26.1 and contribute nothing on newer versions.

Restoring them means moving the text onto the matching `BlockItem`, which changes how those
blocks are registered. That is deliberate outstanding work, not an oversight.

**The GuiGraphics rework, broken down**

`GuiGraphics` became `GuiGraphicsExtractor` and `Screen.render` became
`Screen.extractRenderState`. The eighty-eight errors are less daunting than they look, because
most of the drawing vocabulary survived. Across seventeen files:

| Call | 26.1 | Risk |
|---|---|---|
| `drawString(Font, …, x, y, colour)` | `text(…)` | none, identical arguments |
| `drawCenteredString(…)` | `centeredText(…)` | none, identical arguments |
| `enableScissor` / `disableScissor` | unchanged | none |
| `fill(x1, y1, x2, y2, colour)` | unchanged | none |
| `pose().pushPose()` / `popPose()` | `pushMatrix()` / `popMatrix()` | none |
| `pose().translate(x, y, 0)` | `translate(x, y)` | none, every call passes zero |
| `pose().scale(x, y, 1)` | `scale(x, y)` | none, every call passes one |
| `blitSprite(…)` | takes a `RenderPipeline` first | pick the right pipeline |
| `pose()` held as a `PoseStack` | now a two-dimensional `Matrix3x2fStack` | see below |

The renames are done. `GuiGraphics` to `GuiGraphicsExtractor`, `drawString` to `text` and
`drawCenteredString` to `centeredText` are rewritten while building, which cleared every
GuiGraphics symbol error. The text renames are anchored to their receiver because this
codebase also draws with `java.awt.Graphics2D`, whose own `drawString` must not be touched.

The scissor and fill calls needed nothing. The transform calls in
`BetaWarningScreen` and `FakePauseScreen` are safe too, because they all pass zero for the
translation's third axis and one for the scale's, so flattening to two dimensions loses
nothing.

Three places do need a decision:

- `GuiHelper.drawText` translates by a **variable** z to layer text. Two dimensions have no
  third axis, and 26.1 orders the interface by draw order rather than depth, so this needs
  re-expressing rather than translating.
- `Drawing` has a `Drawing(PoseStack, RenderType)` constructor that two widgets feed
  `context.pose()` into. It needs a two-dimensional counterpart.
- Four widgets hold `context.pose()` in a `PoseStack` local and pass it around.

Note that `DrivingGuiRenderer` and `BlockEntityRendererExtension` also use a `PoseStack`, but
theirs comes from world rendering rather than from `GuiGraphics`, and is unaffected.

The `Drawing` case is worse than a missing constructor, and it joins the two GUI widgets to the
same problem the world renderer has. `Drawing(PoseStack, RenderType)` resolves its buffer with
`RenderType.gui()`, and 26.1 has neither piece: `RenderType` no longer offers a GUI variant at
all, and interface drawing goes through `RenderPipelines.GUI`, `GUI_TEXTURED` and `GUI_TEXT`
instead. JOML offers no conversion from `Matrix3x2f` to `Matrix4f` either, so the matrix cannot
simply be widened.

So the custom drawing path is not portable by adaptation. It has to move onto the pipeline
model, which is the same change the world renderer needs. Treating them as one piece of work
rather than two is likely to be less effort, not more, because they end up sharing the same
approach to buffers, pipelines and uniforms.

**Block colour handlers**

`BlockColor` became `BlockTintSource`, and the registration changed on both loaders at once.
The whole mapping, verified against the jars:

| Old | New |
|---|---|
| `BlockColor.getColor(BlockState, BlockAndTintGetter, BlockPos, int)` | `BlockTintSource.colorInWorld(BlockState, BlockAndTintGetter, BlockPos)` |
| Fabric `ColorProviderRegistry.BLOCK.register(handler, blocks)` | `BlockColorRegistry.register(List<BlockTintSource>, Block...)` |
| NeoForge `RegisterColorHandlersEvent.Block` | `RegisterColorHandlersEvent.BlockTintSources` |

Both loaders now take the same `register(List<BlockTintSource>, Block...)` shape, so the two
registration paths converge rather than diverging further.

Two things are not mechanical. The tint index is gone, which costs this mod nothing because
the existing handler ignored it. More importantly `BlockTintSource` has an abstract
`color(BlockState)` alongside the position-aware default, so the current lambda has to become
a real implementation, and something has to be decided for the case with no position, which is
what inventory and particle rendering use. Station colouring is derived from the position, so
that fallback is a genuine choice rather than a transcription, and it is visible in the game
rather than in the build.

**Block entity persistence moves to ValueInput and ValueOutput**

`loadAdditional` and `saveAdditional` no longer take a `CompoundTag` and a
`HolderLookup.Provider`; they take `ValueInput` and `ValueOutput`. This affects the twenty-six
`readNbt` and `writeNbt` implementations across thirteen classes.

`ValueInput` exposes the same accessor shape the new `CompoundTag` does, `getStringOr`,
`getIntOr`, `getBooleanOr` and the rest, so the bodies already converted for the `Optional`
change carry over nearly unaltered. Only the signatures need guarding.

Long arrays are the trap. `ValueOutput` has `putIntArray` but no `putLongArray`, and
`ValueInput` has `getIntArray` but no `getLongArray`, while this mod stores its identifiers as
longs: platform ids, route ids, railway sign selections and lift track floor positions. Those
have to move to `store(key, codec, value)` and `read(key, codec)`.

**Use `Codec.LONG_STREAM`, not `Codec.LONG.listOf()`.** Both compile and both round-trip within
a single version, but they do not write the same NBT. `NbtOps` implements the `createLongList`
and `getLongStream` hooks, so a `LONG_STREAM` codec produces a `LongArrayTag`, which is exactly
what `putLongArray` wrote before. A list codec produces a `ListTag` of `LongTag` instead, and
every world saved by an older version silently loses those values on load: a passenger
information display forgets its platforms, a train sensor forgets its routes, a railway sign
forgets its selections.

Nothing in the build catches this. It appears only as data quietly missing after an upgrade,
so verify it by loading a world saved on 1.21.4 rather than a freshly created one.

Treat this as needing a client despite looking mechanical. It is the save and load path, so a
mistake does not fail to compile and does not misdraw; it silently loses a player's block data
on the next world reload. Verify by placing configured blocks, restarting the world, and
confirming their settings survived.

**Render passes own the frame**

The single rule behind four separate failures here, and the one to check first when a new screen
or renderer misbehaves. From 26.1 a render pass is exclusive: while one is open the command
encoder refuses everything that is not a bind or a draw. Uploading a texture, mapping a buffer
and clearing a target are all commands, and all of them throw.

It caught the interface image drawing, the model texture binding, the per model transform
uniform and the per map tile transform uniform, in that order, each one only once the previous
was fixed and the next code path could run.

That matters because the calls which trigger them do not look like commands at the call site:

- `TextureManager.getTexture(id)` loads and uploads the texture the first time it is asked.
- `DynamicUniforms.writeTransform(...)` maps a buffer to write into.
- `ReleasedDynamicTexture.getDynamicGlId()` uploads before returning the identifier.

So resolve every texture, write every uniform and finish every upload **before** opening the
pass, and leave the pass holding nothing but binds and draws. Where a batch needs one uniform
per instance, `DynamicUniforms.writeTransforms(...)` writes them all at once and hands back one
slice per instance; that is what it exists for.

The failure is badly signposted. It throws inside vanilla with a message about render passes and
no hint of which mod opened one, and if the pass is left open the next frame dies somewhere else
entirely, often in the renderer's own clear. None of it appears at compile time, and the mod's
own interface code hit it as readily as the world renderer did.

**The pipeline declares the vertex layout**

The sibling of the rule above, and the second thing to check when geometry misbehaves rather than
crashes. Up to 26.1 a `VertexBuffer` carried its own `VertexFormat` and set the attribute pointers
from it, so a mesh packed in a layout that was merely a **superset** of what the shader read still
drew correctly. From 26.1 the layout comes from `RenderPipeline.getVertexFormat()` and the mesh's
own format is never consulted, so the two have to agree exactly.

Layers that look interchangeable are not:

| Layer | Format | Vertex |
|---|---|---|
| `entityCutout`, `entityTranslucent*` | `DefaultVertexFormat.ENTITY` | 36 bytes |
| `beaconBeam` | `DefaultVertexFormat.BLOCK` | 32 bytes, no overlay element |
| `lines` | `DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH` | carries the width per vertex |

`NewOptimizedModel` packed every mesh as `ENTITY` while `MoreRenderLayers` drew the light stages
through `beaconBeam`. Read at 32 bytes instead of 36, every position after the first came out of
the middle of the vertex before it — packed colour and texture bits reinterpreted as floats — which
put a spike through the sky at every car of a train while the car bodies, drawn through entity
layers, were perfect. The mesh is now packed in `renderLayer.format()`, and the stage to layer
mapping lives in `MoreRenderLayers.get` so that the build and the draw ask the same question.

The same rule cuts the other way for a buffered draw: a vertex missing an element the format
declares is refused outright rather than defaulted. The line layer's new width element took the
client down the first time the mod drew a line, which needs only a brush, a lift tool or a rail
item in hand; `IDrawing.drawLineInWorld` writes the width now.

Note that `DefaultVertexFormat.NEW_ENTITY` was renamed to `ENTITY`; there is no separate `ENTITY`
of the older kind to confuse it with. Writing an element the format does not have — `setOverlay`
into a `BLOCK` buffer — is silently skipped by `BufferBuilder` rather than failing, so packing for
the narrower format is safe.

The way to tell this apart from bad geometry is to rule the geometry out. Log the source
coordinates at the call site and scan the built `MeshData` vertex buffer for out-of-range floats;
if both are clean and the picture is not, the mesh and the pipeline disagree about the layout.

**The interface pipeline has no depth test, and the buffer flushes late**

The third rule of the family, and the one behind a map that was black only when something was
on it. `RenderPipeline.Builder.build()` resolves an unset depth-stencil state to none, and the
interface snippet never sets one, so `RenderPipelines.GUI` and `GUI_TEXTURED` neither test nor
write depth. A z offset between two interface draws, which the old `RenderType.gui()` honoured,
now means nothing.

That matters because `Drawing`, and anything else writing through the shared buffer source, does
not draw: the batch waits until a different render type is requested or `endBatch()` is called.
Anything drawn immediately in between — a render pass, a stored mesh — ends up underneath the
batch when it finally flushes. The dashboard map drew its background into the buffer, its tiles
through a pass, and its stations into the same batch as the background, so the first label
flushed background and stations together on top of the tiles. Before 26.1 the tiles sat at z = 1
and won the depth test against the late background, which is why upstream never saw it.

Where an immediate draw has to sit above buffered work, flush the buffer first:
`Minecraft.getInstance().renderBuffers().bufferSource().endBatch()`. Flush again before lifting
a scissor, because the clip is read when a batch is drawn rather than when it is written. The
tell is a picture that is right when some element is absent and clobbered when it is present:
the element's presence is what triggers the flush.

**Recipes, now closed**

All 340 recipes failed to parse on 26.1. MTR writes ingredients in the object form 1.21.1 reads,
`{"item": "minecraft:glass_pane"}` and `{"tag": "c:redstone_dusts"}`, while 26.1 reads only the
string form, `"minecraft:glass_pane"` and `"#c:redstone_dusts"`. An object-form ingredient is
dropped without complaint, the list comes out empty, and the recipe is rejected with
`List is too short: 0, expected range [1-9]` — so the error names the symptom, not the cause.
1.21.4 reads both forms, which is why it never showed.

The source keeps the object form, because 1.21.1 accepts nothing else, and the 26.1 nodes
rewrite the files as `processResources` copies them, through `RecipeIngredientFilter` in
`buildSrc`, which parses the JSON rather than matching text. One ingredient also changed name:
`minecraft:chain` became `minecraft:iron_chain` when copper chains arrived, and it is mapped in
the same filter. Confirm the target form against the game's own data, not memory —
`data/minecraft/recipe/glass_pane.json` in each version's client jar shows it, and
`iron_chain.json` shows the rename.

Note that Gradle did not consider the new filter an input change and reported
`processResources` up to date on the first run; a clean build applies it.

**Finish line**

The mod builds and runs on both loaders, a train completes a route, block entity settings
survive a world reload, and recipes work. All four now hold. What keeps the section open is the
list under *State*: the two text sites, and the breadth of the mod past a single line.

**Pitfall**

Do **not** reach for a build-time replacement to fix a compile error without first checking
who else calls the method. Of roughly two hundred `getString` callers here, only eight read
NBT; the rest are translation holders and the schema reader. A blanket rewrite would have
silently corrupted them. The same applies to any token short enough to appear inside an
unrelated name: rewriting `Identifier` in reverse would have mangled `formatIdentifier` and
a log message that mentions the word in prose.

Note also that a runtime check cannot guard a Gradle task accessor. Referring to `remapJar`
directly stops the build script compiling on unobfuscated versions, because the type-safe
accessor is generated only while the remapping Loom variant is applied. Resolve such tasks
by name with an explicit type instead.

**Packaging, now closed**

The Fabric 26.1.2 jar used to ship without its shaded libraries. With no remap step the plain
`jar` task becomes the mod jar, and the Shadow output was not wired into it, so the release jar
held the mod and its assets and none of the four and a half thousand library classes it needs.
`buildAndCollect` now ships the shaded jar directly on unobfuscated versions, which is what the
NeoForge build already did on every version.

The first Fabric launch found the other half of that same problem. Loom nests the `include`d
jars — UniversalCraft, Elementa, the Kotlin standard library — into *its* mod jar and writes the
`jars` entry into `fabric.mod.json` as it does so. On the remapping variant that jar is `remapJar`,
which is fed the shaded jar and therefore ends up complete. On unobfuscated versions the plain
`jar` task is the mod jar, and it knew nothing of the shaded libraries while the shaded jar knew
nothing of the nesting; shipping the shaded half meant no UniversalCraft, and the first screen
failed. The plain jar now takes the shaded jar's contents in place of the compiled output — the
whole of it, because shading relocates the occlusion culling library and rewrites the callers to
match, so the compiled classes alone would name it where it no longer is — and Loom nests into
that. Note that Loom resets the jar task's duplicate strategy after configuration, so the
compiled output is excluded by path rather than deduplicated.

Three further packaging traps, all found by running the artefact rather than building it:

- The shaded jar carried seven `META-INF/services` entries naming classes that Transport
  Simulation Core's own minimisation had removed. NeoForge builds a module descriptor from the
  mod jar on 26.1 and refuses one whose services it cannot resolve, so the game stopped during
  mod scanning. Those registrations are now excluded.
- Stonecutter's generated copy of the resources was not a faithful one. On roughly one run in
  three it wrote a single 8 KiB block of some large file from 4 KiB further on — a different file
  each time, the fonts most often at up to 18 MiB — and serial runs were no better than parallel
  ones, so it is the plugin's own copy and not Gradle's scheduling. It was fatal only because
  26.1 rasterises every glyph a provider declares at reload time rather than lazily, so one
  damaged font stops the game before the title screen. Nothing under the resources carries a
  Stonecutter marker, so the source set now reads `src/main/resources` directly and the
  generated copy is left unused. Still compare the built jar against the source before shipping
  it; all 4889 resources should match byte for byte apart from the rewritten recipes.

---

## How to use this document

- Before starting a refactor on any code that touches the files referenced above, read the
  relevant section here first. The "Pitfall" rows are concrete.
- When closing out a migration, **remove the section** rather than marking it done —
  `PENDING.md` is the place for completion logs. Keep this file scoped to **active** debt.
- New mid-migration code added to the tree should be accompanied by a new section here in
  the same PR. If a follow-up cannot be filed at the time of merging, the migration is not
  ready to merge.
