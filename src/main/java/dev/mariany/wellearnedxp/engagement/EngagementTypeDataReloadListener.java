package dev.mariany.wellearnedxp.engagement;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.mariany.wellearnedxp.WellEarnedXP;
import dev.mariany.wellearnedxp.registry.WEXDataResources;
import dev.mariany.wellearnedxp.registry.WEXRegistries;
import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.StrictJsonParser;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class EngagementTypeDataReloadListener
        extends SimpleReloadListener<Map<ResourceKey<EngagementTypeData>, EngagementTypeData>> {
    private final HolderLookup.Provider registries;

    public EngagementTypeDataReloadListener(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    protected Map<ResourceKey<EngagementTypeData>, EngagementTypeData> prepare(PreparableReloadListener.SharedState state) {
        FileToIdConverter lister = FileToIdConverter.registry(WEXRegistries.ENGAGEMENT_TYPE);
        RegistryOps<JsonElement> ops = this.registries.createSerializationContext(JsonOps.INSTANCE);
        Map<ResourceKey<EngagementTypeData>, EngagementTypeData> result = new HashMap<>();

        for (Entry<Identifier, Resource> entry : lister.listMatchingResources(state.resourceManager()).entrySet()) {
            Identifier location = entry.getKey();
            Identifier id = lister.fileToId(location);
            ResourceKey<EngagementTypeData> key = ResourceKey.create(WEXRegistries.ENGAGEMENT_TYPE, id);

            try (Reader reader = entry.getValue().openAsReader()) {
                DataResult<EngagementTypeData> parsed = EngagementTypeData.withEntryKey(
                        key,
                        () -> EngagementTypeData.CODEC.parse(ops, StrictJsonParser.parse(reader))
                );

                parsed.ifSuccess(data -> {
                    if (result.putIfAbsent(key, data) != null) {
                        throw new IllegalStateException("Duplicate engagement type data file ignored with ID " + id);
                    }
                }).ifError(error -> WellEarnedXP.LOGGER.error(
                        "Couldn't parse engagement type data file '{}' from '{}': {}",
                        id,
                        location,
                        error
                ));
            } catch (JsonParseException | IllegalArgumentException | IOException error) {
                WellEarnedXP.LOGGER.error(
                        "Couldn't parse engagement type data file '{}' from '{}'",
                        id,
                        location,
                        error
                );
            }
        }

        return result;
    }

    @Override
    protected void apply(
            Map<ResourceKey<EngagementTypeData>, EngagementTypeData> prepared,
            PreparableReloadListener.SharedState state
    ) {
        state.get(DataResourceLoader.DATA_RESOURCE_STORE_KEY)
             .put(WEXDataResources.ENGAGEMENT_TYPES, Map.copyOf(prepared));
    }
}
