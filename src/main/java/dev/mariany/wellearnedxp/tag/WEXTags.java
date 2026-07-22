package dev.mariany.wellearnedxp.tag;

import dev.mariany.wellearnedxp.WellEarnedXP;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class WEXTags {
    private WEXTags() {
    }

    public static final class Blocks {
        public static TagKey<Block> AGE_CROP_HARVESTABLE = createTag("age_crop_harvestable");
        public static TagKey<Block> CROP_HARVESTABLE = createTag("crop_harvestable");

        private Blocks() {
        }

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, WellEarnedXP.id(name));
        }
    }
}
