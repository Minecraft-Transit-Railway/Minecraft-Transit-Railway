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

## How to use this document

- Before starting a refactor on any code that touches the files referenced above, read the
  relevant section here first. The "Pitfall" rows are concrete.
- When closing out a migration, **remove the section** rather than marking it done —
  `PENDING.md` is the place for completion logs. Keep this file scoped to **active** debt.
- New mid-migration code added to the tree should be accompanied by a new section here in
  the same PR. If a follow-up cannot be filed at the time of merging, the migration is not
  ready to merge.
