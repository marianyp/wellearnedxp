package dev.mariany.wellearnedxp.registry;

import dev.mariany.wellearnedxp.WellEarnedXP;
import dev.mariany.wellearnedxp.engagement.EngagementTypeData;
import dev.mariany.wellearnedxp.engagement.EngagementTypeDataReloadListener;
import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;
import net.fabricmc.fabric.api.resource.v1.DataResourceStore;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.Map;
import java.util.function.Function;

public final class WEXDataResources {
    public static final DataResourceStore.Key<Map<ResourceKey<EngagementTypeData>, EngagementTypeData>>
            ENGAGEMENT_TYPES = new DataResourceStore.Key<>();

    private WEXDataResources() {
    }

    public static void bootstrap() {
        WellEarnedXP.bootstrapLog("Data Resources");

        register("engagement_types", EngagementTypeDataReloadListener::new);
    }

    private static void register(String name, Function<HolderLookup.Provider, PreparableReloadListener> factory) {
        DataResourceLoader.get().registerReloadListener(WellEarnedXP.id(name), factory);
    }
}
