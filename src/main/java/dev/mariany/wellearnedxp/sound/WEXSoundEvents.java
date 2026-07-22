package dev.mariany.wellearnedxp.sound;

import dev.mariany.wellearnedxp.WellEarnedXP;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class WEXSoundEvents {
    public static final SoundEvent ENGAGEMENT_REWARD_PICKUP = register("engagement_reward.pickup");

    private static SoundEvent register(final String id) {
        return register(WellEarnedXP.id(id));
    }

    private static SoundEvent register(final Identifier id) {
        return register(id, id);
    }

    private static SoundEvent register(final Identifier id, final Identifier soundId) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(soundId));
    }

    public static void bootstrap() {
        WellEarnedXP.bootstrapLog("Sound Events");
    }
}
