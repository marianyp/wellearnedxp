package dev.mariany.wellearnedxp.mixin;

import dev.mariany.wellearnedxp.stat.handler.BlocksPlacedStatHandler;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancements/triggers/ItemUsedOnLocationTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemInstance;)V"
            )
    )
    public void injectPlace(BlockPlaceContext placeContext, CallbackInfoReturnable<InteractionResult> cir) {
        BlocksPlacedStatHandler.onBlockPlace(placeContext);
    }
}
