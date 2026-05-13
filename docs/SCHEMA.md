# Schema

> Schema-driven code generation used by Minecraft Transit Railway.

## Why schema generation exists

MTR maintains shared data models that must stay consistent across:

- Java runtime code in `common/`
- TypeScript UI code in `website/`

To avoid hand-maintaining duplicate model classes, the project generates source code from JSON schema definitions.

## Schema sources

Authoring files live in:

- `buildSrc/src/main/resources/schema/config/`
- `buildSrc/src/main/resources/schema/resource/`
- `buildSrc/src/main/resources/schema/legacy/`

## Generated outputs

| Target                 | Output path                                        |
|------------------------|----------------------------------------------------|
| Java                   | `common/src/main/java/org/mtr/generated/`          |
| Java (config subset)   | `common/src/main/java/org/mtr/generated/config/`   |
| Java (resource subset) | `common/src/main/java/org/mtr/generated/resource/` |
| TypeScript             | `website/src/app/entity/generated/`                |

## Regeneration workflow

From repository root:

```powershell
.\gradlew.bat :common:setupFiles
.\gradlew.bat :common:setupWebsiteFiles
```

`setupFiles` performs Java generation and import fix-ups used by this repository's shaded package layout.

## Rules

- Do not hand-edit generated files.
- Edit schema source files, then regenerate.
- Commit both schema and generated output changes together.

## Validation checklist after schema edits

1. Regenerate Java and TypeScript outputs.
2. Build `common` and `website`.
3. Verify downstream code compiles without manual post-edits.
4. Confirm no unrelated generated files changed unexpectedly.
