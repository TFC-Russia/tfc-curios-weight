<!--
The text of the mod's page on CurseForge, kept here so it lives under git rather
than only on the site.

The CurseForge API cannot write it: its token covers uploading files and editing
a file's own metadata, nothing about the project itself. So this file is the
source of truth and the page is updated by pasting it in, under
Manage Project -> Description.

The same text is on both branches, because one page covers both game versions.

The project's other fields, which are also web-only:

  Name     TFC Curios Weight
  Summary  Adds weight calculation (weight debuffs) for Curios slots
  License  MIT
  Source   https://github.com/TFC-Russia/tfc-curios-weight
  Issues   https://github.com/TFC-Russia/tfc-curios-weight/issues
  Avatar   docs/avatar.png in this repository (256x256; CurseForge asks for 400x400)
  Page     https://www.curseforge.com/minecraft/mc-mods/tfc-curios-weight, project id 1120348
-->

# TFC Curios Weight

TerraFirmaCraft makes you Exhausted while you carry one huge and very heavy item and Overburdened
while you carry two — but it only ever looks in your inventory. This addon makes it look in your
Curios slots as well, so a backpack worn on your back counts like one carried in a slot.

## What it does

- Items worn in Curios slots are counted together with the ones in your inventory. Two huge and very
  heavy items between them, wherever they sit, and you are Overburdened.
- **One of them worn in a Curios slot is free.** With nothing huge and very heavy in your inventory,
  a single such item in a Curios slot gives no debuff at all, so you can wear one huge backpack and
  still walk normally. A second one anywhere brings the debuffs back.
- Nothing else changes. The effects, the thresholds and the item sizes are TFC's own, and the addon
  adds no items, no recipes and no interface of its own.

TFC's `enableOverburdening` still governs all of this, and players in creative are skipped.

## Config

`config/tfc_curios_weight-common.toml`:

| Option | Default | What it does |
|---|---|---|
| `enable_curios_slots_weight_check` | `true` | Whether Curios slots are counted at all. Off leaves TFC's own behaviour |
| `calculate_weight_each_n_ticks` | `20` | How often the check runs. 20 ticks is one second |
| `curios_slots` | `[]` | Not implemented yet. Every slot is counted |

## Requirements

| Minecraft | Loader | TerraFirmaCraft | Curios API |
|---|---|---|---|
| 1.20.1 | Forge 47+ | 3.2.7+ | 5.10.0+ |
| 1.21.1 | NeoForge 21.1.0+ | 4.2.0+ | 9.5.0+ |

Both are required: without Curios there are no slots to count.

## See also

[**TFC Item Weight**](https://www.curseforge.com/minecraft/mc-mods/tfc-item-weight) gives every item
a weight and shows how loaded you are as a bar above the TFC food bar, Curios slots included. The two
fit together: this addon keeps TFC's huge-and-very-heavy rule honest across Curios slots, while the
bar tracks everything else you carry.

## License

MIT: you can include it in your modpack without asking permission. Copyright (C) 2024-2026
koenigstag.

TerraFirmaCraft, which this is an addon for, is under the EUPL, and the Curios API is under the LGPL.
When using portions of this mod, be aware that the TFC licence may apply; both licences are on their
own mod pages.

Source: [github.com/TFC-Russia/tfc-curios-weight](https://github.com/TFC-Russia/tfc-curios-weight).
