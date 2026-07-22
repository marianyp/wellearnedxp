package dev.mariany.wellearnedxp;

import dev.mariany.wellearnedxp.datagen.WEXBlockTagsProvider;
import dev.mariany.wellearnedxp.datagen.WEXEngagementTypeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class WellEarnedXPDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(WEXBlockTagsProvider::new);
        pack.addProvider(WEXEngagementTypeProvider::new);
    }
}
