package dev.mariany.wellearnedxp.mixin;

import dev.mariany.wellearnedxp.engagement.EngagementRewardCandidate;
import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin implements EngagementRewardCandidate {
}
