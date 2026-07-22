package dev.mariany.wellearnedxp.particle;

import dev.mariany.wellearnedxp.WellEarnedXP;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class WEXParticleTypes {
    public static final SimpleParticleType EXPERIENCE = register("experience", false);

    private static SimpleParticleType register(String id, boolean alwaysSpawn) {
        return Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                WellEarnedXP.id(id),
                FabricParticleTypes.simple(alwaysSpawn)
        );
    }

    public static void bootstrap() {
        WellEarnedXP.bootstrapLog("Particle Types");
    }
}
