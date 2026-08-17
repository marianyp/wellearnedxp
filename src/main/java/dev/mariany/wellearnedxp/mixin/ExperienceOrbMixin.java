package dev.mariany.wellearnedxp.mixin;

import dev.mariany.wellearnedxp.engagement.AwardeeHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin implements AwardeeHolder {
    @Inject(method = "playerTouch", at = @At(value = "HEAD"), cancellable = true)
    public void injectPlayerTouch(Player player, CallbackInfo ci) {
        if (this.wellearnedxp$lacksAwardee()) {
            return;
        }

        if (this.wellearnedxp$isAwardee(player)) {
            return;
        }

        ci.cancel();
    }
}
