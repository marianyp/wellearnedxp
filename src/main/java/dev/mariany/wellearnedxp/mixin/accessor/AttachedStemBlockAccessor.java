package dev.mariany.wellearnedxp.mixin.accessor;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AttachedStemBlock.class)
public interface AttachedStemBlockAccessor {
    @Accessor("fruit")
    ResourceKey<Block> wellearnedxp$fruit();
}
