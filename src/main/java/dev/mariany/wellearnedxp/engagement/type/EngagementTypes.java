package dev.mariany.wellearnedxp.engagement.type;

import dev.mariany.wellearnedxp.WellEarnedXP;
import dev.mariany.wellearnedxp.registry.WEXRegistries;
import dev.mariany.wellearnedxp.stat.WEXStats;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stats;

public final class EngagementTypes {
    private EngagementTypes() {
    }

    public static void bootstrap(Registrar registrar) {
        register(registrar, CustomStatEngagementType.builder(Stats.RAID_WIN).experience(55));
        register(registrar, StatTypeEngagementType.builder(Stats.BLOCK_MINED).experience(0, 6).interval(25));
        register(registrar, StatTypeEngagementType.builder(Stats.ITEM_BROKEN).experience(16));

        register(registrar, CustomStatEngagementType.builder(WEXStats.BLOCKS_PLACED).experience(0, 6).interval(25));
        register(registrar, CustomStatEngagementType.builder(WEXStats.CROPS_HARVESTED).experience(0, 4));
        register(registrar, CustomStatEngagementType.builder(WEXStats.DIFFERENT_ITEMS_CRAFTED).experience(4));
        register(registrar, CustomStatEngagementType.builder(WEXStats.LOOT_DISCOVERED).experience(7, 16));
    }

    private static void register(Registrar registrar, EngagementType.Builder<?, ?, ?> builder) {
        EngagementType<?> engagementType = builder.build();
        registrar.register(key(engagementType.getName()), EngagementTypeData.of(engagementType));
    }

    private static ResourceKey<EngagementTypeData> key(final String id) {
        return ResourceKey.create(WEXRegistries.ENGAGEMENT_TYPE, WellEarnedXP.id(id));
    }

    @FunctionalInterface
    public interface Registrar {
        void register(ResourceKey<EngagementTypeData> key, EngagementTypeData data);
    }
}
