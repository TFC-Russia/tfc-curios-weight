package com.koenigstag.tfc_curios_weight;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        private static final ForgeConfigSpec.BooleanValue ENABLE_CURIOS_SLOTS_CHECK = BUILDER
                        .comment("Whether to calculate overweight effects checking Curios slots. Disables the whole purpose of this addon")
                        .define("enable_curios_slots_weight_check", true);

        public static final ForgeConfigSpec.IntValue CALCULATE_WEIGHT_EACH_N_TICKS = BUILDER
                        .comment("Calculate weight every N ticks. Default: 20 ticks = 1 sec")
                        .defineInRange("calculate_weight_each_n_ticks", 20, 1, Integer.MAX_VALUE);

        private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SLOT_NAME_STRINGS = BUILDER
                        .comment("WIP. A list of slots to check in weight calculation.")
                        .defineListAllowEmpty("curios_slots", List.of(), Config::validateSlotName);

        static final ForgeConfigSpec SPEC = BUILDER.build();

        // Per world, in world/serverconfig: Forge sends it to every client that joins, so a client that counts the
        // items itself (the weight message of TFC Item Weight, tfc_weight_bar) follows the server's rule rather than a file of its own
        private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        private static final ForgeConfigSpec.IntValue HOW_MANY_HUGE_HEAVY_ITEMS_OVERBURDEN = SERVER_BUILDER
                        .comment("How many huge and very heavy items carried in the player's inventory overburden the player.",
                                        "2: TFC's rule, one such item exhausts and two overburden. 1: a single one overburdens.",
                                        "TFC itself overburdens at 2 whatever this says, so higher values would change nothing.",
                                        "Armor worn in its armor slot and a shield in the off hand are not counted as carried here: they still count as before.",
                                        "Curios slots are not affected: one such item worn there is still free.")
                        .defineInRange("how_many_huge_heavy_items_overburdens", 2, 1, 2);

        static final ForgeConfigSpec SERVER_SPEC = SERVER_BUILDER.build();

        public static boolean enableWeightCalculations;
        public static List<ISlotType> curios_slots;
        public static int calculateWeightEachNTicks;
        public static int hugeHeavyItemsToOverburden = 2;

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {
                final Object spec = event.getConfig().getSpec();

                if (spec == SERVER_SPEC) {
                        // Unloading: the world is closing, and its rule goes with it until the next world loads its own
                        hugeHeavyItemsToOverburden = event instanceof ModConfigEvent.Unloading
                                        ? 2
                                        : HOW_MANY_HUGE_HEAVY_ITEMS_OVERBURDEN.get();
                        return;
                }

                if (spec != SPEC) {
                        return;
                }

                enableWeightCalculations = ENABLE_CURIOS_SLOTS_CHECK.get();

                curios_slots = SLOT_NAME_STRINGS.get().stream()
                                .map(slotId -> CuriosApi.getSlot(slotId, getIsClient()).get())
                                .collect(Collectors.toList());

                calculateWeightEachNTicks = CALCULATE_WEIGHT_EACH_N_TICKS.get();
        }

        private static boolean getIsClient() {
                // TODO fix this. Needed in CuriosApi.getSlot(String id, boolean isClient)
                return false;
        }

        private static boolean validateSlotName(final Object obj) {
                return obj instanceof final String slotId
                                && CuriosApi.getSlot(slotId, getIsClient()).isPresent();
        }
}
