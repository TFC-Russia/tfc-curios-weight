package com.koenigstag.tfc_curios_weight;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandlerModifiable;

import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import net.dries007.tfc.common.capabilities.size.IItemSize;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.dries007.tfc.util.Helpers;

public class CuriosHelpers {
  public static int countOverburdened(final Player livingEntity) {
    int overweightCount = 0;

    Container container = livingEntity.getInventory();

    // Counts player inventory to the full weight
    overweightCount += Helpers.countOverburdened(container);

    if (Config.enableWeightCalculations == false) {
      return overweightCount;
    }

    // Counts Curios slots to the full weight
    if (ModList.get().isLoaded("curios")) {
      LazyOptional<ICuriosItemHandler> lazyCuriosInventory = CuriosApi.getCuriosInventory(livingEntity);

      if (lazyCuriosInventory.isPresent()) {
        ICuriosItemHandler curiosInventory = lazyCuriosInventory.resolve().get();
        IItemHandlerModifiable curiosContainer = curiosInventory.getEquippedCurios();

        for (int i = 0; i < curiosContainer.getSlots(); i++) {
          final ItemStack stack = curiosContainer.getStackInSlot(i);

          if (!stack.isEmpty()) {
            IItemSize size = ItemSizeManager.get(stack);

            if (size.getWeight(stack) == Weight.VERY_HEAVY
                && size.getSize(stack) == Size.HUGE) {
              overweightCount++;

              if (overweightCount == 2) {
                return overweightCount;
              }
            }
          }
        }
      }
    }

    return overweightCount;
  }
}
