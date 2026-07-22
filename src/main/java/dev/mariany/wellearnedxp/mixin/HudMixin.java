package dev.mariany.wellearnedxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.mariany.wellearnedxp.client.WellEarnedXPClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class HudMixin {
    @WrapOperation(
            method = "extractHotbarAndDecorations",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/contextualbar/ContextualBar;extractExperienceLevel(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;I)V"
            )
    )
    private void wrapExtractHotbarAndDecorations(
            GuiGraphicsExtractor graphics,
            Font font,
            int experienceLevel,
            Operation<Void> original
    ) {
        original.call(graphics, font, experienceLevel);
        WellEarnedXPClient.ENGAGEMENT_REWARD_EFFECTS.renderExperienceLevelOverlay(graphics, experienceLevel);
    }
}
