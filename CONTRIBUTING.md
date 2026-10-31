# Contributing

Pull requests are welcome, and so are issues: a suggestion or a bug report is just as useful.

## Branches

The two game versions live side by side, each on its own branch. There is no `main`.

| Branch | Minecraft | Loader | Java |
| --- | --- | --- | --- |
| `1.20.x` | 1.20.1 | Forge 47.3.0 | 17 |
| `1.21.x` | 1.21.1 | NeoForge 21.1.265 | 21 |

Open a pull request against the branch your change belongs to. For a change that applies to both, one pull request per branch is easiest to review.

## Building

The required Java version differs per branch. Each branch pins it in `.jabbarc`, so [jabba](https://github.com/shyiko/jabba) picks it up with no argument:

```
jabba use
./gradlew build
```

Without jabba, point `JAVA_HOME` at a JDK of the version in the table above. The jar lands in `build/libs/`.

## Dependencies

Both branches compile against TerraFirmaCraft and the Curios API. Gradle fetches them, so nothing has to be downloaded by hand:

| Branch | Source | Versions |
| --- | --- | --- |
| `1.20.x` | CurseMaven, by CurseForge file id | TFC 3.2.28, Curios 5.10.0 |
| `1.21.x` | Modrinth maven, by version | TFC 4.2.12, Curios 9.5.1+1.21.1 |

On `1.20.x` Gradle also fetches Patchouli, which TFC requires, but only to run the game with `./gradlew runClient`; the mod itself does not depend on it.

The numbers live in `gradle.properties`. On `1.21.x` bumping a dependency means editing one line; on `1.20.x` it also means looking up the new CurseForge file id.

## Releasing

A GitHub release publishes the mod. `.github/workflows/publish.yml` builds the jar of the commit the release points at and uploads it to [CurseForge](https://www.curseforge.com/minecraft/mc-mods/tfc-curios-weight) and to the release itself.

1. Bump `mod_version` in `gradle.properties` on the branch, as [VERSIONING.md](VERSIONING.md) describes.
2. Create a release on that branch whose tag is `v` plus that version:

   ```
   gh release create v1.0.1+1.20.1 --target 1.20.x --title "1.0.1+1.20.1" --notes "What changed"
   ```

The notes become the CurseForge changelog, and a pre-release goes up as a beta. When the tag and `mod_version` disagree, the workflow stops before uploading anything. The upload needs a CurseForge API token in the `CURSEFORGE_TOKEN` repository secret.

To only build the jar on CI, run the workflow by hand: Actions → Publish → Run workflow.

## Before opening a pull request

- `./gradlew build` passes on the branch you changed.
- `README.md` and `README.ru.md` still say the same thing.
- Code and comments are in English.
