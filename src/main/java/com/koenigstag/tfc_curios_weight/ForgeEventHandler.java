package com.koenigstag.tfc_curios_weight;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.dries007.tfc.config.TFCConfig;

public final class ForgeEventHandler {

  public static void init() {
    final IEventBus bus = NeoForge.EVENT_BUS;

    bus.addListener(ForgeEventHandler::onPlayerTick);
  }

  // PlayerTickEvent.Post fires once, after the tick; the 1.20 event fired on both the START and the END phase
  public static void onPlayerTick(PlayerTickEvent.Post event) {
    final Player player = event.getEntity();
    final Level level = player.level();

    // Copy of the TFC onPlayerTick but with CuriosHelper calculations
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
