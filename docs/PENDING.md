# Pending work and progress log

> Living checklist of partially-done refactors, deferred follow-ups, and tracked
> improvements. Previously lived in `CODE_STYLES.md` sections 5 and 6 — moved here so
> `CODE_STYLES.md` stays a pure conventions document.
>
> When something on this list is done, move it to the **Completed** section with a one-line
> commit/PR reference. When a new follow-up is identified during code review, add it under
> **Known follow-ups** with enough detail that a future contributor can act on it without
> re-deriving context.

## Completed style enforcement

These items were addressed in earlier modernisation passes:

- ✅ **Package-level JavaDoc** — every package under `common/src/main/java/org/mtr/` carries a
  concise, descriptive `package-info.java` per §3.7 of `CODE_STYLES.md`.
- ✅ **Import grouping** — all Java and TypeScript sources were resorted per §2.6:
  - Java: framework / library → third-party / shadowed → project (`org.mtr.*`).
  - TypeScript: Angular framework → Material / third-party → services / components / entities.
- ✅ **Class-level JavaDoc on the load-bearing classes** — 12+ canonical classes across
  `common/` carry a paragraph explaining their role.
- ✅ **Angular component file split** — every component is `name.component.{ts,html,scss}`;
  no inline `template:` / `styles:` blocks remain.
- ✅ **Deprecated API removal**:
  - `new URL(String)` → `URI.toURL()` (Java 21 best practice).
  - Removed unused `getRightClickText()`, `getVehicleByIndex()`, `incrementTestDuration()`.
- ✅ **Unused field cleanup** — removed public search fields (`DASHBOARD_SEARCH`,
  `ROUTES_PLATFORMS_SEARCH`, …); made appropriate boolean fields `final`; added
  `@Getter` to static instances per `CODE_STYLES.md` §3.6.
- ✅ **Java 21 idioms** — pattern matching in `switch`, records for value types, sealed
  interfaces for ADTs in new code.
- ✅ **Backend terminology** — documentation aligned with this codebase's current structure.

## Known follow-ups

### Style / hygiene

- **Replace remaining `MTR.LOGGER.error("", e)` sites with descriptive messages** — §3.14 of
  `CODE_STYLES.md` forbids empty-message logging. As of this writing, twelve sites still
  use the empty form (already fixed: `CustomResourceLoader`, `DynamicTextureCache`,
  `ObjModelLoader`, `ResourceManagerHelper`, `RouteMapGenerator` first three sites).
  Remaining sites:
  - `common/src/test/java/org/mtr/test/BlockbenchModelValidationTest.java:28`
  - `buildSrc/src/main/java/org/mtr/BuildTools.java:139, 220, 233, 237, 254, 257, 262`
  - `common/src/main/java/org/mtr/cache/CachedFileResource.java:43, 95`
  - `common/src/main/java/org/mtr/cache/CachedFileProvider.java:30`
  - `common/src/main/java/org/mtr/config/Config.java:50, 69`
  - `common/src/main/java/org/mtr/legacy/resource/CustomResourcesConverter.java:33, 45, 64, 81`

  Each must name the operation that failed (e.g. `"Failed to load custom resources from {}"`,
  `identifier`).

- **Additional class-level JavaDoc** — many lower-traffic utility classes (e.g.
  `org.mtr.tool.*`, `org.mtr.cache.*`) would benefit from a one-paragraph header. Add
  incrementally during normal edits — do not block PRs on missing doc on classes the PR
  doesn't touch.

- **Further Lombok adoption** — candidates with hand-written constructors / accessors that
  could be `@RequiredArgsConstructor` / `@Getter`:
  - `org.mtr.render.PositionAndRotation`
  - `org.mtr.render.StoredMatrixTransformations`
  - several POJO-shaped classes under `org.mtr.data.*`.

  Caveats per §3.6: schema-generated classes in `org.mtr.generated.*` stay Lombok-free.

- **Sealed types** — complex enumerations like `RenderStage`, `QueuedRenderLayer`,
  `RailType`, `PartCondition` could be migrated to sealed interfaces with per-variant
  records where pattern matching would simplify dispatch. Existing implementations work
  and the migration is opt-in.

- **Pattern matching in `switch`** — several call sites in `MainRenderer.render`
  (`switch (queuedRenderLayer)`) and `ObjModelLoader.getRenderStage` already use Java 21
  arrow-form switch expressions. A few legacy `switch` statements remain — convert when
  touching the surrounding code.

### Performance and architecture

The full performance backlog is in [`PERFORMANCE.md`](PERFORMANCE.md). Highest-value items:

- **`WorkerThread.dynamicTextureQueue` bug** — currently a single-slot `AtomicReference` that
  drops all but the last submitted runnable. See [`PERFORMANCE.md` §2.1](PERFORMANCE.md).
- **`GraphicsEnvironment.getAllFonts()` called per missing character** — see
  [`PERFORMANCE.md` §2.2](PERFORMANCE.md).
- **Main-thread `setColorArgb` pixel-copy loop** — see [`PERFORMANCE.md` §2.4](PERFORMANCE.md).
- **`MODEL_RENDERS` nested-map churn** — see [`PERFORMANCE.md` §3.1](PERFORMANCE.md).
- **Eager OBJ / bbmodel parsing in `VehicleModel` constructor** — see
  [`PERFORMANCE.md` §1.2 / §1.3](PERFORMANCE.md).
- **`RESOURCE_CACHE` is unbounded** — see [`PERFORMANCE.md` §1.1 / §1.4](PERFORMANCE.md).

Track each item by linking the PR back to the relevant `PERFORMANCE.md` section in the
commit message.
