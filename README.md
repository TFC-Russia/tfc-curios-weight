# TFC Curios Weight

## About

This is a simple TFC addon that adds a weight calculations to the TFC mod for Curios slots. It is a copy of the same calculations for player inventory from TFC but for Curios slots.

## Requirements

- This mod requires the Curios API v5.10.0+
- This mod requires TerraFirmaCraft v3.2.7 for mc 1.20+

## Changelog

### 1.0.0
- Initial release of the mod. It adds weight calculations for all Curios slots.
- There is a config option to disable the mod.
- There is special case when Player will not receive TFC Exhausted effect - when **at most one** item in Curios slots is HUGE + VERY_HEAVY, allowing to carry one heavy backpack in Curios slot without a debuff. Any other combinations of HUGE + VERY_HEAVY items will lead to debuffs.

## To Do List

- [ ] Implement a config option to change the list of Curios slots to calculate weight for.

## Contributing

PRs are welcome. You may also open an issue if you have any suggestions or found a bug.

## License

This mod is under the MIT License. This means that you can include it in your modpack without asking my permission.

TFC, which this mod is an addon for, is under the EUPL license. When using portions of this mod, be aware that the TFC license may apply. You can view that license on that mod page.

Curios, which this mod depends on, is under the LGPL. You can view that license on that mod page.
