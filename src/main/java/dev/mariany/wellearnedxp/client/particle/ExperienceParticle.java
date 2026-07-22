package dev.mariany.wellearnedxp.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class ExperienceParticle extends SingleQuadParticle {
    private ExperienceParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xa,
            double ya,
            double za,
            TextureAtlasSprite sprite
    ) {
        super(level, x, y, z, xa, ya, za, sprite);

        this.xd = xa;
        this.yd = 0.1 + ya;
        this.zd = za;

        this.friction = 0.96F;
        this.gravity = -0.1F;
        this.speedUpWhenYMotionIsBlocked = true;

        this.quadSize *= 0.75F;
        this.lifetime = (int) (8 / (this.random.nextFloat() * 0.8F + 0.2F));
        this.hasPhysics = false;

        this.alpha = 0;
    }

    @Override
    protected Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    @Override
    protected int getLightCoords(float partialTickTime) {
        return LightCoordsUtil.FULL_BRIGHT;
    }

    @Override
    public void tick() {
        super.tick();

        float progress = Mth.clamp(
                (float) this.age / (float) this.lifetime,
                0,
                1
        );

        this.alpha = Mth.lerp(progress * 0.5F, 0, 1);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(final SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public @Nullable Particle createParticle(
                SimpleParticleType options,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xAux,
                double yAux,
                double zAux,
                RandomSource random
        ) {
            ExperienceParticle particle = new ExperienceParticle(
                    level,
                    x,
                    y,
                    z,
                    xAux,
                    yAux,
                    zAux,
                    this.sprite.get(random)
            );

            particle.setColor(1, 1, 1);

            return particle;
        }
    }
}
