package com.koenigstag.tfc_curios_weight;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;

public final class ForgeEventHandler {

  public static void init() {
    final IEventBus bus = MinecraftForge.EVENT_BUS;

    bus.addListener(ForgeEventHandler::onPlayerTick);
  }

  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    // When facing up in the rain, player slowly recovers thirst.
    final Player player = event.player;
    final Level level = player.level();

    // Copy of TFC's onPlayerTick but with CuriosHelper calculations
    if (!level.isClientSide() && !player.getAbilities().invulnerable && TFCConfig.SERVER.enableOverburdening.get()
        && level.getGameTime() % Config.calculateWeightEachNTicks == 0) {
      final int hugeHeavyCount = CuriosHelpers.countOverburdened(player);

      if (hugeHeavyCount >= 2) {
        player.addEffect(CuriosHelpers.getOverburdened(false));
      } else if (hugeHeavyCount == 1) {
        player.addEffect(CuriosHelpers.getExhausted(false));
      }
    }
  }
}
