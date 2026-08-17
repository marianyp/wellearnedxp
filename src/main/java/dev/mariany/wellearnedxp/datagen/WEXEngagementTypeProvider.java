package dev.mariany.wellearnedxp.datagen;

import dev.mariany.wellearnedxp.engagement.type.EngagementTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class WEXEngagementTypeProvider extends EngagementTypeProvider {
    public WEXEngagementTypeProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    public void generateEngagementTypes(HolderLookup.Provider registryLookup, EngagementTypes.Registrar registrar) {
        EngagementTypes.bootstrap(registrar);
    }

    @Override
    public String getName() {
        return "Well-Earned XP Engagement Types";
    }
}
