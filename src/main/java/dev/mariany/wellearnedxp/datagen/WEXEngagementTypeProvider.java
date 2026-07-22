package dev.mariany.wellearnedxp.datagen;

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
    public String getName() {
        return "Well-Earned XP Engagement Types";
    }
}
