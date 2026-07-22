package dev.mariany.wellearnedxp.client.particle;

import dev.mariany.wellearnedxp.particle.WEXParticleTypes;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public final class WEXParticleResources {
    private WEXParticleResources() {
    }

    public static void bootstrap() {
        register(WEXParticleTypes.EXPERIENCE, ExperienceParticle.Provider::new);
    }

    private static <T extends ParticleOptions> void register(
            ParticleType<T> type,
            ParticleProviderRegistry.PendingParticleProvider<T> provider
    ) {
        ParticleProviderRegistry.getInstance().register(type, provider);
    }
}
