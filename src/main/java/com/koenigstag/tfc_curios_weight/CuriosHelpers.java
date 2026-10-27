package com.koenigstag.tfc_curios_weight;

import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import net.dries007.tfc.common.component.size.IItemSize;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;
import net.dries007.tfc.common.effect.TFCEffects;

import java.util.Optional;

public class CuriosHelpers {
  public static int countOverburdened(final Player livingEntity) {
    int overweightCount = 0;

    Container container = livingEntity.getInventory();

    // Counts player inventory to the full weight
    overweightCount += countOverburdened(container);

    if (Config.enableWeightCalculations == false) {
      return overweightCount;
    }

    int curiosWeightCount = 0;

    // Counts Curios slots to the full weight
    if (ModList.get().isLoaded("curios")) {
      // Curios 9 returns a plain Optional; Curios 5 handed out a Forge LazyOptional
      Optional<ICuriosItemHandler> maybeCuriosInventory = CuriosApi.getCuriosInventory(livingEntity);

      if (maybeCuriosInventory.isPresent()) {
        ICuriosItemHandler curiosInventory = maybeCuriosInventory.get();
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

  /**
   * TFC 4.x no longer ships Helpers.countOverburdened, so the count lives here: one per item that is both HUGE and
   * VERY_HEAVY, over every slot of the container.
   */
  public static int countOverburdened(Container container) {
    int count = 0;

    for (int i = 0; i < container.getContainerSize(); i++) {
      count += getItemStackWeightInt(container.getItem(i));
    }

    return count;
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
    // MobEffectInstance takes a Holder in 1.21, and the TFC effect id carries a DeferredHolder
    return new MobEffectInstance(TFCEffects.OVERBURDENED.holder(), Config.calculateWeightEachNTicks + 5, 0, false,
        visible);
  }

  public static MobEffectInstance getExhausted(boolean visible) {
    return new MobEffectInstance(TFCEffects.EXHAUSTED.holder(), Config.calculateWeightEachNTicks + 5, 0, false,
        visible);
  }
}
