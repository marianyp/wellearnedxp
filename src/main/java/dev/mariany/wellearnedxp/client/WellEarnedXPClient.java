package dev.mariany.wellearnedxp.client;

import dev.mariany.wellearnedxp.WellEarnedXP;
import dev.mariany.wellearnedxp.client.particle.WEXParticleResources;
import dev.mariany.wellearnedxp.config.ConfigHandler;
import dev.mariany.wellearnedxp.config.WEXClientConfig;
import dev.mariany.wellearnedxp.packet.clientbound.ClientBoundPackets;
import net.fabricmc.api.ClientModInitializer;

public class WellEarnedXPClient implements ClientModInitializer {
    public static final EngagementRewardEffects ENGAGEMENT_REWARD_EFFECTS = new EngagementRewardEffects();

    private static final ConfigHandler<WEXClientConfig> CONFIG_HANDLER = new ConfigHandler<>(
            WellEarnedXP.MOD_ID + "-client",
            new WEXClientConfig()
    );

    public static WEXClientConfig getConfig() {
        return CONFIG_HANDLER.getConfig();
    }

    @Override
    public void onInitializeClient() {
        CONFIG_HANDLER.loadConfig();
        ENGAGEMENT_REWARD_EFFECTS.bootstrap();
        WEXParticleResources.bootstrap();
        ClientBoundPackets.bootstrap();
    }
}
