# Code Styles

> Conventions enforced across the Minecraft Transit Railway codebase. New rules land here as
> they're agreed.

These rules apply to **both** the Java code in `common/src/main/java/org/mtr/...` and the
Angular frontend in `website/` unless otherwise noted. Anything not listed here defers to the
project's compiler/linter settings (Java 21 `javac` warnings, Angular CLI/TypeScript checks).

For an overview of the system the rules apply to, see [ARCHITECTURE.md](ARCHITECTURE.md).

## 0. Build System

This project uses **Gradle Kotlin DSL** (`.gradle.kts` files). Do not use Groovy DSL syntax
in new build logic. When editing build scripts, prefer:
- Type-safe accessors (`tasks.jar { ... }` not `tasks.named("jar") { ... }`)
- Explicit types for task configurations (`tasks.withType<JavaCompile>()`)
- Named arguments for filter/map operations (see existing `generateVersion` task)

## 1. Naming

### 1.1 Package / folder names are singular

- ✅ `org.mtr.core.data`, `org.mtr.core.servlet`, `org.mtr.core.simulation`,
  `website/src/app/component/<name>/`, `website/src/app/service/`, `website/src/app/entity/`.
- ❌ `datas/`, `services/`, `entities/`, `components/`.

### 1.2 No abbreviations in identifiers

Package names, class / interface / record names, method names, field names, parameter names,
and local variable names must spell out their meaning in full.

- ✅ `routes.forEach((route) => { … })`
- ❌ `routes.forEach((r) => { … })`
- ✅ `for (final Station station : simulator.stations) { … }`
- ❌ `for (final Station st : simulator.stations) { … }`

**Allowed exceptions:**

| Context           | Allowed                                        | Example                                       |
|-------------------|------------------------------------------------|-----------------------------------------------|
| Caught exceptions | `e`, `ex`                                      | `} catch (Exception e) { … }`                 |
| Transloco let     | `t`                                            | `*transloco="let t"`                          |
| Loop indices      | `i`, `j`, `k`                                  | `for (int i = 0; i < length; i++) { … }`      |
| Domain idioms     | `id`, `dto`, `uuid`                            | These are de-facto whole words in our domain. |
| Geometry / colour | `x`, `y`, `z`, `r`, `g`, `b`, `dx`, `dy`, `dz` | Minecraft world coordinates and RGB channels. |
| Library type echo | match the imported class                       | `Path path = Paths.get(...)`                  |

The "library type echo" rule: when a local needs to hold a value of an imported type, prefer
the type's own name in lowerCamelCase as the variable name (`Path path`,
`Simulator simulator`, `JsonReader jsonReader`, `Webserver webserver`). It removes a class of
bikeshedding and makes refactor-rename across the file trivial.

### 1.3 Casing

- **Java classes / records, TypeScript classes / interfaces / types**: `PascalCase`.
- **Java methods, fields, locals, parameters; TypeScript methods, fields, locals, parameters,
  signals**: `camelCase`.
- **Java `static final` constants and TypeScript module-level `const`s used as constants**:
  `SCREAMING_SNAKE_CASE` (e.g. `Main.MILLISECONDS_PER_TICK`,
  `OperationProcessor.UPDATE_RIDING_ENTITIES`).
- **Operation / message keys exchanged over the wire** — `lower_snake_case`
  (e.g. `get_data`, `update_riding_entities` from
  `OperationProcessor` keys from the `org.mtr:transport-simulation-core` dependency.
  The OBA REST endpoints are `kebab-case` because that is the
  [OneBusAway specification](https://developer.onebusaway.org/api/where).
- **CSS / SCSS class names**: `kebab-case`.
- **Transloco i18n keys**: `camelCase`. Same no-abbreviation rule as identifiers
  (`directions.routesAlternative`, **not** `directions.routesAlt`). See
  [`website/src/assets/i18n/en.json`](../website/src/assets/i18n/en.json).

### 1.4 British English in prose, American English in identifiers

- All Markdown docs (`README.md`, `docs/*.md`) and all source-code comments use **British
  English**: `colour`, `behaviour`, `centre`, `localise`, `organise`, `analyse`.
- All code identifiers — class names, method names, variable names, signal names, i18n keys,
  CSS class names — use **American English**: `color`, `behavior`, `center`, `localize`,
  `organize`, `analyze`.

The split keeps reader-facing copy consistent with Hong Kong / UK readers, while keeping API
and code identifiers on the de-facto American convention every framework and library already
uses (`Color`, `localStorage`, `behavior` events …). When in doubt: prose-British,
code-American.

## 2. Formatting

### 2.1 Indentation

- **Tabs**, not spaces. Java, TypeScript, HTML, SCSS, JSON (including the schema files under
  `buildSrc/src/main/resources/schema/`). One tab per nesting level.
- Continuation lines inside chained calls / multi-argument calls (see §2.2) are also indented
  with tabs, one level beyond their parent.

YAML files in this repo (for example `.github/workflows/*.yml`) must use 2 spaces per level —
YAML forbids tabs at indentation positions.

### 2.2 No mid-expression line wrapping

Lines stay on a single line. Don't pre-emptively wrap arguments or method bodies.

**Allowed exceptions** — chains and many-argument calls.

#### Chained streams / pipes

```ts
this.httpClient.get<{ data: StationsAndRoutes }>("/mtr/api/map/stations-and-routes")
	.pipe(
		tap((response) => this.stationsAndRoutes.set(response.data)),
		map((response) => response.data.routes.length),
	)
	.subscribe();
```

```java
return simulator.routes.stream()
	.filter(route -> route.getRouteType() == routeType)
	.map(NameColorDataBase::getHexId)
	.collect(Collectors.toCollection(ObjectArrayList::new));
```

Each chained call gets its own line, indented one tab beyond the receiver.

#### Method calls with many parameters

```ts
this.mapDataService.requestArrivals(
	dimension,
	stationIds,
	platformIds,
	maxCountPerPlatform,
);
```

```java
clients.put(clientId, new Client(
		clientId,
		Main.CLIENT_NAME_RESOLVER == null ? "" : Main.CLIENT_NAME_RESOLVER.apply(client.uuid),
		client.getPosition().getX(),
		client.getPosition().getZ(),
		simulator.stations.stream().filter(station -> station.inArea(client.getPosition())).map(NameColorDataBase::getHexId).findFirst().orElse("")
));
```

The opening paren is on the call line; arguments are indented one tab; the closing paren
sits at the original indent level on its own line (or with the last argument).

### 2.3 Trailing newline

Every file ends with **exactly one** trailing newline (one blank line at EOF, no more).

### 2.4 Always brace control statements

Every `if`, `else`, `else if`, `for`, `while`, and `do` block uses braces — even when the
body is a single statement. This keeps diffs sane when a second statement is added later
and removes a class of "dangling-else" / accidental scope bugs.

```java
// ✅
if (depot == null) {
	return;
}

for (final Route route : routes) {
	route.tick();
}

// ❌
if (depot == null) return;
for (final Route route : routes) route.tick();
```

```ts
// ✅
if (this.routes().length === 0) {
	return;
}

// ❌
if (this.routes().length === 0) return;
```

The single allowed exception is a lambda / arrow-function expression body — `route -> route.getId()`,
`(value) => value * 2` — which is an expression, not a control statement.

### 2.5 String quotes

- **TypeScript**: double quotes `"…"` for ordinary strings; backticks for template literals
  only when interpolating.
- **Java**: standard `"…"`; text blocks (`"""…"""`) only for genuinely multi-line literals.
- **HTML attributes**: double quotes only.
- **JSON**: double quotes (RFC 8259 — there is no choice).

### 2.6 Imports

Group imports by **origin**, separated by a blank line. Within each group, sort
**alphabetically by module path**. The same rule applies to the `imports: [...]` array in
every Angular `@Component({ ... })` decorator — keep it alphabetised so additions land at a
predictable spot.

The grouping order, top to bottom (skip groups that don't apply):

1. **Angular framework** — `@angular/*`.
2. **Third-party packages** — `@jsverse/transloco`, `@iconify-icons/*`, `iconify-icon`,
   `primeng/*`, `rxjs`, `three`, …
3. **Internal modules** — `../core/...`, `../component/...`, anything under `website/src/app/`.

```ts
import {ChangeDetectionStrategy, Component, inject} from "@angular/core";
import {FormsModule} from "@angular/forms";

import {TranslocoDirective} from "@jsverse/transloco";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";

import {MapDataService} from "../../service/map-data.service";
import {RoutePanelComponent} from "../route-panel/route-panel.component";
```

The same rule applies to Java imports — JDK `java.*` / `javax.*` last (matching IntelliJ's
default), library imports above, project (`org.mtr.core.*`) imports first. JSpecify
annotation imports (`org.jspecify.annotations.NullMarked`,
`org.jspecify.annotations.Nullable`) live with the other third-party imports.

No re-export barrels (`index.ts`) unless absolutely necessary; prefer direct imports so
refactor tools can move files cleanly.

### 2.7 No dead SCSS

Every selector in a component's `.scss` file must match a class actually used by the matching
`.html` file — or be referenced by runtime-injected DOM (PrimeNG drawer overlays,
`iconify-icon` shadow DOM, Three.js DOM siblings). When a rule targets runtime DOM, leave a
comment naming the source so the next reader doesn't assume it's dead and delete it.

When you remove markup, delete the now-orphaned SCSS in the same change. Run
`npm run build` periodically — the production CSS extractor surfaces unused rules through
bundle-size growth.

## 3. Architecture

### 3.1 Constants live in their own files

Magic numbers in service / component / servlet code are forbidden. Lift them to either:

- **Backend**: a `static final` constant on the closest owning class
  (e.g. `Main.MILLISECONDS_PER_TICK`,
  `SystemMapServlet.STATIONS_AND_ROUTES_CACHE_MILLIS`, `Webserver.MAX_THREADS`), or a
  dedicated `*Constants` class if the value is used across packages.
- **Frontend**: a `website/src/app/utility/<area>.constants.ts` file exporting
  `SCREAMING_SNAKE_CASE` constants.

The rule of thumb: "if an operator or designer might tweak this, it goes in a constants
file." If a tunable lives on both sides (e.g. tick rate, refresh interval), the comment must
say so on both ends.

### 3.2 Reusable UI

Common UI shapes (panels, toggles, list entries, etc.) live in their own component under
`website/src/app/component/<name>/`. Build features by composing these — don't copy markup.
Modularise aggressively, **even if a piece is only used once**: it keeps the parent
component's TypeScript focused on logic and lets the template stay declarative.

The existing `data-list-entry`, `interchange-style-toggle`, `visibility-toggle` and `title`
components are the canonical examples.

### 3.3 Angular component file layout

Always split an Angular component into **three sibling files**:

```
component/<name>/
├── <name>.component.ts
├── <name>.component.html
└── <name>.component.scss
```

No `template:` or `styles:` inline blocks in `@Component(...)` — even one-liners. Always use
`templateUrl` and `styleUrl`. This keeps diffs clean and makes IDE template tooling work.

### 3.4 PrimeNG severities for status colour

Use PrimeNG's `severity` palette (`info`, `success`, `warn`, `danger`, `secondary`) and
design tokens (`var(--p-text-muted-color)`, `var(--p-content-border-color)`, …) for status /
chrome colour. **Custom hex colours are reserved for domain colour codes** — the per-route
colours from MTR / OBA data, station / interchange marker colours, etc.

### 3.5 Tooltips on icon-only affordances

Every icon-only button (no visible label) MUST have a `pTooltip` describing its action. Same
for any icon plotted on the map without a textual label (station markers, interchange glyphs,
direction arrows). The tooltip text must come from the i18n bundle, not be hardcoded.

### 3.6 Lombok yes, Spring no

This codebase is intentionally framework-light: it ships as a plain runnable jar with
embedded Jetty so the Minecraft Transit Railway mod can drop it in without dragging Spring's
classloader along.

- **No Spring** — wire dependencies through plain constructors. The HTTP layer is Jetty
  servlets wired through the shared mod/runtime integration.
- **No reflection-based DI / bean scanning** — every component is `new`'d explicitly.
- **Lombok is allowed and encouraged** where it cleans things up:
  - `@RequiredArgsConstructor` on classes whose constructor is just `this.x = x; this.y = y;`
    over `final` fields.
  - `@Log4j2` instead of direct static logger access in classes that do their own logging.
    Use `@Log4j2`, **not** `@Slf4j` — this project logs through Log4j2 directly.
	- `@Getter` / `@Setter` / `@Value` / `@Builder` / `@Data` on POJOs and DTO-shaped
	  classes where the boilerplate is otherwise pure noise. Records are still preferred for
	  immutable value types in Java 21.
  - The schema-generated classes under
    [`common/src/main/java/org/mtr/generated/`](../common/src/main/java/org/mtr/generated/)
    are Lombok-free by design — they're produced by generation tasks in `buildSrc/`.

### 3.7 JSpecify nullness

Every Java package has a `package-info.java` annotated with `@NullMarked` from
[JSpecify](https://jspecify.dev/). Everything is non-null by default; mark nullable values
explicitly with `@Nullable` (also from `org.jspecify.annotations`).

```java
@NullMarked
package org.mtr.core.servlet;

import org.jspecify.annotations.NullMarked;
```

- Do not use `Optional` for fields or method parameters — `Optional` is for return values
  only (Java's official guidance). Use `@Nullable` instead.
- The schema-generated classes under
  [`common/src/main/java/org/mtr/generated/`](../common/src/main/java/org/mtr/generated/) and
  the generated `package-info.java` files are also JSpecify-annotated; the generator
  task emits the correct imports automatically.
- The frontend's TypeScript counterpart is `strictNullChecks: true` (already on by default
  in `website/tsconfig.json`).

### 3.8 fastutil over JDK collections

For the simulation hot path, prefer fastutil's primitive-keyed and primitive-valued
collections — they avoid boxing on every put / get, which matters at MILLISECONDS_PER_TICK =
10 ms ticks.

- `Object2ObjectAVLTreeMap` / `Object2ObjectOpenHashMap` instead of `HashMap` / `TreeMap`.
- `Long2ObjectAVLTreeMap`, `Object2IntOpenHashMap`, etc. when keys / values are primitives.
- `ObjectArrayList`, `ObjectImmutableList`, `LongArrayList` for typed lists.
- `ObjectLists.emptyList()` instead of `Collections.emptyList()`.

Use JDK collections only when crossing a public API boundary that already commits to JDK
types (e.g. records returned to Gson serialisation, where Gson handles `List` cleanly but
not fastutil's `ObjectList`).

### 3.9 Internationalised strings

- All player-visible text in the Angular frontend lives in
  `website/src/assets/i18n/<lang>.json` (`en.json`, `zh.json`).
- Hardcoding English strings in templates / TypeScript is forbidden.
- **Translation-key parity is mandatory.** Every key present in `en.json` must also be
  present in `zh.json` (and any future locale). The Transloco resolver falls back to English
  at runtime, but a missing key in a non-English locale is a bug — review the diff before
  merging.

### 3.10 Locale-aware UI formatting

All numbers, dates and times rendered in the UI must adapt to the **browser's locale**
(`navigator.language`), not the active translation language. Reach for the existing pipes
in [`website/src/app/pipe/`](../website/src/app/pipe/) — `formatDatePipe.ts`,
`formatTimePipe.ts` — instead of calling `Intl.NumberFormat` / `Intl.DateTimeFormat` from
templates.

### 3.11 Schema-driven code is generated, not hand-written

Anything under `common/src/main/java/org/mtr/generated/` and
`website/src/app/entity/generated/` is produced by the `Generator` task in `buildSrc/`. **Do
not edit these files by hand** — they will be overwritten on the next build. Edit the
authoring JSON schema under `buildSrc/src/main/resources/schema/<area>/` instead, then run
`./gradlew :common:setupFiles` and `./gradlew :common:setupWebsiteFiles`. The schema workflow
and the regen workflow are documented in [`SCHEMA.md`](SCHEMA.md).

### 3.12 Style guides

- **Angular**: follow <https://angular.dev/style-guide> where it fits this repository's
  current Angular version and tooling.
- **Java**: idiomatic Java 21 — records for value types, sealed interfaces for ADTs, pattern
  matching in `switch`, `var` only where the right-hand side type is obvious from context.
  Stick to platform threads (the simulator already runs each dimension on its own scheduled
  thread); virtual threads are acceptable for genuinely I/O-bound auxiliary work.
- **TypeScript**: idiomatic ES2024+ — `const` by default, `readonly` on every interface field
  that doesn't need to be mutated, template literal types where they buy clarity, `satisfies`
  over type assertion.

### 3.13 Resolve all IDE warnings

Treat compiler / IntelliJ / TS-Server / ESLint warnings as errors. If a warning is genuinely
not applicable, suppress it locally with the narrowest possible annotation
(`@SuppressWarnings("…")`, `// eslint-disable-next-line …`) and a one-line comment explaining
why. Don't broad-suppress at the file or module level.

### 3.14 Don't swallow exceptions silently

`catch (Exception ignored) {}` is forbidden. At minimum, log the swallowed exception at
`debug` level (Java: a Lombok `@Log4j2`-injected `log.debug(...)` per §3.6; TypeScript:
`console.debug`) with a one-line comment naming the recovery path. This makes "why did
nothing happen?" debugging tractable months later.

## 4. Documentation

- High-level system overview: [`ARCHITECTURE.md`](ARCHITECTURE.md).
- HTTP/local servlet reference: [`API.md`](API.md).
- Build and development workflow: [`BUILD.md`](BUILD.md).
- Runtime and packaging usage: [`RUNNING.md`](RUNNING.md).
- Schema and generated-code workflow: [`SCHEMA.md`](SCHEMA.md).
- Performance findings and frame-time backlog: [`PERFORMANCE.md`](PERFORMANCE.md).
- In-flight style / refactor follow-ups and completed-work log: [`PENDING.md`](PENDING.md).
- Keep the top-level [`README.md`](../README.md) as a concise project overview and route
  deep operational details into `docs/`.
