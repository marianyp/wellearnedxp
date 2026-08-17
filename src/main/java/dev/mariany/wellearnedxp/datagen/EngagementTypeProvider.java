package dev.mariany.wellearnedxp.datagen;

import dev.mariany.wellearnedxp.engagement.type.EngagementTypeData;
import dev.mariany.wellearnedxp.engagement.type.EngagementTypes;
import dev.mariany.wellearnedxp.registry.WEXRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class EngagementTypeProvider extends FabricCodecDataProvider<EngagementTypeData> {
    public EngagementTypeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, WEXRegistries.ENGAGEMENT_TYPE, EngagementTypeData.CODEC);
    }

    @Override
    protected void configure(
            BiConsumer<Identifier, EngagementTypeData> provider,
            HolderLookup.Provider registryLookup
    ) {
        this.generateEngagementTypes(
                registryLookup,
                (key, data) -> provider.accept(key.identifier(), data)
        );
    }

    public abstract void generateEngagementTypes(
            HolderLookup.Provider registryLookup,
            EngagementTypes.Registrar registrar
    );
}
