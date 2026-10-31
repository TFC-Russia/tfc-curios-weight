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

A GitHub release publishes the mod. `.github/workflows/publish.yml` builds the jar of the commit the release points at and uploads it to [CurseForge](https://www.curseforge.com/minecraft/mc-mods/tfc-curios-weight) and to the release itself. `scripts/release.sh` sets the version and creates that release:

1. On the release branch, add a `## <version>` entry at the top of both `CHANGELOG.md` and `CHANGELOG.ru.md`, and commit it.
2. Run the script with the mod's own version, without the game version:

   ```
   bash scripts/release.sh 1.0.2
   ```

The script stops before changing anything when the branch is not a release branch, has uncommitted changes, is behind `origin`, when the version is not newer than `mod_version` or its tag already exists, or when either changelog lacks the entry. Otherwise it shows the plan and asks before it:

- sets `mod_version` to `1.0.2+<minecraft version>`, as [VERSIONING.md](VERSIONING.md) describes, and commits that;
- pushes the branch;
- creates the release `v1.0.2+<minecraft version>` with the `CHANGELOG.md` entry as its notes.

| Option | Effect |
| --- | --- |
| `--dry-run` | Run the checks and show the plan, change nothing |
| `--prerelease` | Publish a pre-release, which goes up to CurseForge as a beta |
| `--yes` | Skip the question |

The script needs git and the GitHub CLI (`gh`) signed in to an account that can push to the repository. On Windows, run it from Git Bash.

The release notes become the CurseForge changelog. When the tag and `mod_version` disagree, the workflow stops before uploading anything. The upload needs a CurseForge API token in the `CURSEFORGE_TOKEN` repository secret.

To only build the jar on CI, run the workflow by hand: Actions → Publish → Run workflow.

## Before opening a pull request

- `./gradlew build` passes on the branch you changed.
- `README.md` and `README.ru.md` still say the same thing, and so do `CHANGELOG.md` and `CHANGELOG.ru.md`.
- Code and comments are in English.
