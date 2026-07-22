package dev.mariany.wellearnedxp.registry;

import dev.mariany.wellearnedxp.WellEarnedXP;
import dev.mariany.wellearnedxp.engagement.EngagementTypeData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class WEXRegistries {
    public static final ResourceKey<Registry<EngagementTypeData>> ENGAGEMENT_TYPE = createRegistryKey("engagement_type");

    private WEXRegistries() {
    }

    private static <T> ResourceKey<Registry<T>> createRegistryKey(final String name) {
        return ResourceKey.createRegistryKey(WellEarnedXP.id(name));
    }
}
