# TFC Curios Weight

**English** · [Русский](README.ru.md)

## About

This is a simple TFC addon that adds a weight calculations to the TFC mod for Curios slots. It is a copy of the same calculations for player inventory from TFC but for Curios slots.

## Requirements

- Minecraft 1.21.1 on NeoForge 21.1.0+ (built against 21.1.265)
- TerraFirmaCraft 4.2.0+ (built against 4.2.12)
- Curios API 9.5.0+ (built against 9.5.1+1.21.1)

## Changelog

### 1.0.0
- Initial release of the mod. It adds weight calculations for all Curios slots.
- There is a config option to disable the mod.
- There is special case when Player will not receive TFC Exhausted effect - when **at most one** item in Curios slots is HUGE + VERY_HEAVY, allowing to carry one heavy backpack in Curios slot without a debuff. Any other combinations of HUGE + VERY_HEAVY items will lead to debuffs.

## To Do List

- [ ] Implement a config option to change the list of Curios slots to calculate weight for.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for the branch layout, how to build and what to check before opening a pull request.

## License

This mod is under the MIT License. This means that you can include it in your modpack without asking my permission.

TFC, which this mod is an addon for, is under the EUPL license. When using portions of this mod, be aware that the TFC license may apply. You can view that license on that mod page.

Curios, which this mod depends on, is under the LGPL. You can view that license on that mod page.
