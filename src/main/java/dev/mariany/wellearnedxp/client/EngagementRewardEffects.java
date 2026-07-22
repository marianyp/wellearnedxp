package dev.mariany.wellearnedxp.client;

import dev.mariany.wellearnedxp.engagement.EngagementRewardCandidate;
import dev.mariany.wellearnedxp.particle.WEXParticleTypes;
import dev.mariany.wellearnedxp.sound.WEXSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBar;
import net.minecraft.client.gui.contextualbar.ExperienceBar;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EngagementRewardEffects {
    private static final Identifier EXPERIENCE_BAR_PROGRESS_SPRITE = Identifier.withDefaultNamespace(
            "hud/experience_bar_progress"
    );

    private static final int EXPERIENCE_TEXT_COLOR = 0xFF80FF20;
    private static final int EXPERIENCE_TEXT_HIGHLIGHT_COLOR = 0xFFDEFF9D;
    private static final int EXPERIENCE_BAR_HIGHLIGHT_COLOR = 0xFFFFFFFF;

    private static final double EPSILON = 1.0E-7;
    private static final double PARTICLE_DELTA_DAMPEN_MULTIPLIER = 0.85;
    private static final double FACE_PARTICLE_DISTANCE = 1;

    private final Minecraft minecraft;
    private final int maxRewardEffectTicks;
    private int rewardEffectTicks;

    public EngagementRewardEffects() {
        this(40);
    }

    public EngagementRewardEffects(int maxRewardEffectTicks) {
        this.minecraft = Minecraft.getInstance();
        this.maxRewardEffectTicks = maxRewardEffectTicks;
    }

    public void bootstrap() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(Minecraft client) {
        if (client.isPaused()) {
            return;
        }

        this.tick();
    }

    private void tick() {
        if (this.rewardEffectTicks > 0) {
            this.rewardEffectTicks--;
        }
    }

    public void onPlayerRewarded(Player player) {
        addRewardParticles(player, WEXParticleTypes.EXPERIENCE);

        if (!player.equals(this.minecraft.player)) {
            return;
        }

        this.resetRewardEffect();
    }

    private static void addRewardParticles(Player player, ParticleOptions particle) {
        Vec3 playerDelta = player.getDeltaMovement();
        Vec3 particleDelta = playerDelta.lengthSqr() < EPSILON ? Vec3.ZERO : playerDelta;
        Vec3 dampenedParticleDelta = particleDelta.scale(PARTICLE_DELTA_DAMPEN_MULTIPLIER);

        addSurroundingParticles(player, dampenedParticleDelta, particle);
        addFaceParticle(player, dampenedParticleDelta, particle);
    }

    private static void addSurroundingParticles(Player player, Vec3 delta, ParticleOptions particle) {
        Level level = player.level();
        double playerY = player.getY();

        for (int i = 0; i < 7; i++) {
            double y = switch (i) {
                case 0 -> playerY;
                case 1 -> player.getEyePosition().y;
                default -> player.getRandomY();
            };

            level.addParticle(
                    particle,
                    player.getRandomX(1),
                    y,
                    player.getRandomZ(1),
                    delta.x,
                    0,
                    delta.z
            );
        }
    }

    private static void addFaceParticle(Player player, Vec3 delta, ParticleOptions particle) {
        Level level = player.level();

        Vec3 eyePosition = player.getEyePosition();
        Vec3 lookAngle = player.getLookAngle();
        Vec3 faceParticlePosition = eyePosition.add(lookAngle.scale(FACE_PARTICLE_DISTANCE));

        level.addParticle(
                particle,
                faceParticlePosition.x,
                player.getY(),
                faceParticlePosition.z,
                delta.x,
                0,
                delta.z
        );
    }

    private void resetRewardEffect() {
        this.rewardEffectTicks = this.maxRewardEffectTicks;
    }

    public boolean playPickupSound(ClientLevel clientLevel, @Nullable Entity from, @Nullable Entity to) {
        if (isNotEngagementReward(from) || !WellEarnedXPClient.getConfig().playPickupSound) {
            return false;
        }

        if (to != null && !to.equals(this.minecraft.player)) {
            return false;
        }

        RandomSource randomSource = from.getRandom();

        float pitch = (randomSource.nextFloat() - randomSource.nextFloat()) * 0.35F + 0.9F;

        clientLevel.playLocalSound(
                from.getX(),
                from.getY(),
                from.getZ(),
                WEXSoundEvents.ENGAGEMENT_REWARD_PICKUP,
                SoundSource.PLAYERS,
                0.1F,
                pitch,
                false
        );

        return true;
    }

    private static boolean isNotEngagementReward(@Nullable Entity entity) {
        return !(entity instanceof EngagementRewardCandidate engagementRewardCandidate)
                || !engagementRewardCandidate.wellearnedxp$isEngagementReward();
    }

    public void renderExperienceBarOverlay(ExperienceBar experienceBar, GuiGraphicsExtractor graphics) {
        if (!WellEarnedXPClient.getConfig().renderExperienceBarOverlay) {
            return;
        }

        LocalPlayer player = this.minecraft.player;

        if (player == null || player.getXpNeededForNextLevel() <= 0) {
            return;
        }

        int left = experienceBar.left(this.minecraft.getWindow());
        int top = experienceBar.top(this.minecraft.getWindow());

        int progress = (int) (player.experienceProgress * ContextualBar.WIDTH);

        float intensity = this.getIntensity();

        if (intensity <= 0) {
            return;
        }

        if (progress <= 0) {
            return;
        }

        graphics.blitSprite(
                WEXRenderPipelines.GUI_TEXTURED_ADDITIVE_HIGHLIGHT,
                EXPERIENCE_BAR_PROGRESS_SPRITE,
                ContextualBar.WIDTH,
                ContextualBar.HEIGHT,
                0,
                0,
                left,
                top,
                progress,
                ContextualBar.HEIGHT,
                withIntensity(EXPERIENCE_BAR_HIGHLIGHT_COLOR, intensity)
        );
    }

    private static int withIntensity(int color, float intensity) {
        intensity = Mth.clamp(intensity, 0, 1);
        return ARGB.color(Mth.floor(intensity * 255), ARGB.red(color), ARGB.green(color), ARGB.blue(color));
    }

    public void renderExperienceLevelOverlay(GuiGraphicsExtractor graphics, int experienceLevel) {
        if (!WellEarnedXPClient.getConfig().renderExperienceBarOverlay) {
            return;
        }

        float intensity = this.getIntensity();

        if (intensity <= 0) {
            return;
        }

        Component levelComponent = Component.translatable("gui.experience.level", experienceLevel);

        int x = (graphics.guiWidth() - this.minecraft.font.width(levelComponent)) / 2;
        int y = graphics.guiHeight() - 24 - 9 - 2;

        int color = lerpArgb(intensity, EXPERIENCE_TEXT_COLOR, EXPERIENCE_TEXT_HIGHLIGHT_COLOR);

        graphics.text(this.minecraft.font, levelComponent, x, y, color, false);
    }

    private float getIntensity() {
        if (this.rewardEffectTicks <= 0) {
            return 0;
        }

        float partialTick = this.getPartialTick();

        return Mth.clamp((this.rewardEffectTicks - partialTick) / this.maxRewardEffectTicks, 0, 1);
    }

    private float getPartialTick() {
        DeltaTracker deltaTracker = this.minecraft.getDeltaTracker();
        return deltaTracker.getGameTimeDeltaPartialTick(false);
    }

    private static int lerpArgb(float amount, int from, int to) {
        amount = Mth.clamp(amount, 0, 1);
        int alpha = Mth.floor(Mth.lerp(amount, ARGB.alpha(from), ARGB.alpha(to)));
        int red = Mth.floor(Mth.lerp(amount, ARGB.red(from), ARGB.red(to)));
        int green = Mth.floor(Mth.lerp(amount, ARGB.green(from), ARGB.green(to)));
        int blue = Mth.floor(Mth.lerp(amount, ARGB.blue(from), ARGB.blue(to)));
        return ARGB.color(alpha, red, green, blue);
    }
}
