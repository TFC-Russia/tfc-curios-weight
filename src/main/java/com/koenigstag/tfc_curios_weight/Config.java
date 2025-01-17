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

        public static boolean enableWeightCalculations;
        public static List<ISlotType> curios_slots;
        public static int calculateWeightEachNTicks;

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {
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
