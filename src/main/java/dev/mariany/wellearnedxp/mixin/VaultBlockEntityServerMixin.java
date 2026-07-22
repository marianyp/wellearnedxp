package dev.mariany.wellearnedxp.mixin;

import dev.mariany.wellearnedxp.WellEarnedXP;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VaultBlockEntity.Server.class)
public class VaultBlockEntityServerMixin {
    @Inject(
            method = "tryInsertKey",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/vault/VaultBlockEntity$Server;unlock(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/vault/VaultConfig;Lnet/minecraft/world/level/block/entity/vault/VaultServerData;Lnet/minecraft/world/level/block/entity/vault/VaultSharedData;Ljava/util/List;)V"
            )
    )
    private static void injectTryInsertKey(
            ServerLevel serverLevel,
            BlockPos pos,
            BlockState blockState,
            VaultConfig config,
            VaultServerData serverData,
            VaultSharedData sharedData,
            Player player,
            ItemStack stackToInsert,
            CallbackInfo ci
    ) {
        WellEarnedXP.LOOT_DISCOVERED_STAT_HANDLER.onVaultUnlock(player);
    }
}
