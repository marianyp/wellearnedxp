package dev.mariany.wellearnedxp.mixin;

import dev.mariany.wellearnedxp.WellEarnedXP;
import net.minecraft.advancements.triggers.LootTableTrigger;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootTableTrigger.class)
public class LootTableTriggerMixin {
    @Inject(method = "trigger", at = @At(value = "TAIL"))
    public void injectTrigger(ServerPlayer player, ResourceKey<LootTable> lootTable, CallbackInfo ci) {
        WellEarnedXP.LOOT_DISCOVERED_STAT_HANDLER.onLootTableGenerated(player);
    }
}
