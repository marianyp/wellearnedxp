package dev.mariany.wellearnedxp.mixin;

import dev.mariany.wellearnedxp.client.WellEarnedXPClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ExperienceBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceBar.class)
public class ExperienceBarMixin {
    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void injectExtractBackground(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        ExperienceBar experienceBar = (ExperienceBar) (Object) this;
        WellEarnedXPClient.ENGAGEMENT_REWARD_EFFECTS.renderExperienceBarOverlay(experienceBar, graphics);
    }
}
