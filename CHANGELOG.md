# Changelog

**English** · [Русский](CHANGELOG.ru.md)

Newest version first. The entry of a version is also its GitHub release notes and its changelog on CurseForge.

## 1.0.1
- Ported to Minecraft 1.21.1 on NeoForge. The weight calculations work the same as in 1.0.0.
- The version carries the Minecraft version, and so does the jar name: `tfc_curios_weight-1.0.1+1.21.1.jar`.

## 1.0.0
- Initial release of the mod. It adds weight calculations for all Curios slots.
- There is a config option to disable the mod.
- There is special case when Player will not receive TFC Exhausted effect - when **at most one** item in Curios slots is HUGE + VERY_HEAVY, allowing to carry one heavy backpack in Curios slot without a debuff. Any other combinations of HUGE + VERY_HEAVY items will lead to debuffs.
