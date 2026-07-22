package dev.mariany.wellearnedxp.mixin;

import dev.mariany.wellearnedxp.engagement.EngagementManager;
import dev.mariany.wellearnedxp.stat.handler.DifferentItemsCraftedStatHandler;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatsCounter.class)
public class StatsCounterMixin {
    @Inject(method = "increment", at = @At(value = "HEAD"))
    public void injectIncrementBefore(Player player, Stat<?> stat, int count, CallbackInfo ci) {
        DifferentItemsCraftedStatHandler.beforeStatIncrement(player, stat);
    }

    @Inject(method = "increment", at = @At(value = "TAIL"))
    public void injectIncrementAfter(Player player, Stat<?> stat, int count, CallbackInfo ci) {
        EngagementManager.afterStatIncrement(player, stat);
    }
}
