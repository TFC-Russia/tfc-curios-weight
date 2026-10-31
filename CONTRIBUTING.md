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
| `1.20.x` | CurseMaven, by CurseForge file id | TFC 3.2.7, Curios 5.10.0 |
| `1.21.x` | Modrinth maven, by version | TFC 4.2.12, Curios 9.5.1+1.21.1 |

The numbers live in `gradle.properties`. On `1.21.x` bumping a dependency means editing one line; on `1.20.x` it also means looking up the new CurseForge file id.

## Before opening a pull request

- `./gradlew build` passes on the branch you changed.
- `README.md` and `README.ru.md` still say the same thing.
- Code and comments are in English.
