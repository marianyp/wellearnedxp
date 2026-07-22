package dev.mariany.wellearnedxp.datagen;

import dev.mariany.wellearnedxp.tag.WEXTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;

import java.util.concurrent.CompletableFuture;

public class WEXBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public WEXBlockTagsProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registryLookupFuture
    ) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(WEXTags.Blocks.CROP_HARVESTABLE);
        builder(WEXTags.Blocks.AGE_CROP_HARVESTABLE).add(BlockItemIds.COCOA_CROP).add(BlockItemIds.NETHER_WART);
    }
}
