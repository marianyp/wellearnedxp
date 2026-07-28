package dev.mariany.wellearnedxp.engagement;

import dev.mariany.wellearnedxp.WellEarnedXP;
import dev.mariany.wellearnedxp.registry.WEXRegistries;
import dev.mariany.wellearnedxp.stat.WEXStats;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stats;

public final class EngagementTypes {
    private EngagementTypes() {
    }

    public static void bootstrap(BootstrapContext<EngagementTypeData> context) {
        bootstrap(context::register);
    }

    public static void bootstrap(Registrar registrar) {
        register(registrar, CustomStatEngagementType.builder(Stats.RAID_WIN).experience(55));
        register(registrar, StatTypeEngagementType.builder(Stats.BLOCK_MINED).experience(0, 7).interval(32));
        register(registrar, StatTypeEngagementType.builder(Stats.ITEM_BROKEN).experience(16));

        register(registrar, CustomStatEngagementType.builder(WEXStats.BLOCKS_PLACED).experience(0, 7).interval(32));
        register(registrar, CustomStatEngagementType.builder(WEXStats.CROPS_HARVESTED).experience(0, 2));
        register(registrar, CustomStatEngagementType.builder(WEXStats.DIFFERENT_ITEMS_CRAFTED).experience(1));
        register(registrar, CustomStatEngagementType.builder(WEXStats.LOOT_DISCOVERED).experience(7, 16));
    }

    private static void register(Registrar registrar, EngagementType.Builder<?, ?, ?> builder) {
        EngagementType<?> engagementType = builder.build();
        registrar.register(key(engagementType.getName()), EngagementTypeData.of(engagementType));
    }

    public static void register(
            BootstrapContext<EngagementTypeData> context,
            EngagementType.Builder<?, ?, ?> builder,
            KeyFactory keyFactory
    ) {
        EngagementType<?> engagementType = builder.build();
        String name = engagementType.getName();
        context.register(keyFactory.key(name), EngagementTypeData.of(engagementType));
    }

    private static ResourceKey<EngagementTypeData> key(final String id) {
        return ResourceKey.create(WEXRegistries.ENGAGEMENT_TYPE, WellEarnedXP.id(id));
    }

    @FunctionalInterface
    public interface KeyFactory {
        ResourceKey<EngagementTypeData> key(String id);
    }

    @FunctionalInterface
    public interface Registrar {
        void register(ResourceKey<EngagementTypeData> key, EngagementTypeData data);
    }
}
