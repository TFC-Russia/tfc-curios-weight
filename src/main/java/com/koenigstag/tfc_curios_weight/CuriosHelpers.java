package com.koenigstag.tfc_curios_weight;

import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.dries007.tfc.common.effect.TFCEffects;
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

    int curiosWeightCount = 0;

    // Counts Curios slots to the full weight
    if (ModList.get().isLoaded("curios")) {
      LazyOptional<ICuriosItemHandler> lazyCuriosInventory = CuriosApi.getCuriosInventory(livingEntity);

      if (lazyCuriosInventory.isPresent()) {
        ICuriosItemHandler curiosInventory = lazyCuriosInventory.resolve().get();
        IItemHandlerModifiable curiosContainer = curiosInventory.getEquippedCurios();

        for (int i = 0; i < curiosContainer.getSlots(); i++) {
          final ItemStack stack = curiosContainer.getStackInSlot(i);

          curiosWeightCount += getItemStackWeightInt(stack);

          if (curiosWeightCount >= 2) {
            return curiosWeightCount;
          }
        }
      }
    }

    // Do not count one curios slot (backpack) as exhausting
    // Player can still carry one overweighed slot (e.g. backpack)
    if (overweightCount == 0 && curiosWeightCount == 1) {
      return overweightCount;
    }

    return overweightCount + curiosWeightCount;
  }

  public static int getItemStackWeightInt(ItemStack itemStack) {
    if (!itemStack.isEmpty()) {
      IItemSize size = ItemSizeManager.get(itemStack);

      if (size.getWeight(itemStack) == Weight.VERY_HEAVY
          && size.getSize(itemStack) == Size.HUGE) {
        return 1;
      }
    }

    return 0;
  }

  public static MobEffectInstance getOverburdened(boolean visible) {
    return new MobEffectInstance(TFCEffects.OVERBURDENED.get(), Config.calculateWeightEachNTicks + 5, 0, false, visible);
  }

  public static MobEffectInstance getExhausted(boolean visible) {
    return new MobEffectInstance(TFCEffects.EXHAUSTED.get(), Config.calculateWeightEachNTicks + 5, 0, false, visible);
  }
}
