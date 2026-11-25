package com.koenigstag.tfc_curios_weight;

import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
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

    // how_many_huge_heavy_items_overburdens: that many such items carried (not worn) overburden. The count above
    // includes worn ones, so it is never below the carried count and saves the second pass when it is too low
    final int overburdenAt = Config.hugeHeavyItemsToOverburden;

    if (overweightCount >= overburdenAt && countCarriedHugeHeavyItems(livingEntity.getInventory()) >= overburdenAt) {
      return 2;
    }

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

  /**
   * How many huge and very heavy items the inventory holds that are carried rather than worn. Worn are armor in the
   * armor slot it is made for and a shield in the off hand: a pack can make metal shields huge and very heavy, and
   * with how_many_huge_heavy_items_overburdens = 1 a player holding one up would otherwise be unable to walk.
   */
  public static int countCarriedHugeHeavyItems(final Inventory inventory) {
    int count = 0;

    for (final ItemStack stack : inventory.items) {
      count += getItemStackWeightInt(stack);
    }

    // HUMANOID_ARMOR: 1.21 also has the BODY slot of animal armor, which a player inventory does not hold
    for (final EquipmentSlot slot : EquipmentSlot.values()) {
      if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        count += getCarriedWeightInt(inventory.player, inventory.armor.get(slot.getIndex()), slot);
      }
    }

    return count + getCarriedWeightInt(inventory.player, inventory.offhand.get(0), EquipmentSlot.OFFHAND);
  }

  // getEquipmentSlotForItem became an instance method in 1.20.5
  private static int getCarriedWeightInt(final Player player, final ItemStack stack, final EquipmentSlot slot) {
    return player.getEquipmentSlotForItem(stack) == slot ? 0 : getItemStackWeightInt(stack);
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
