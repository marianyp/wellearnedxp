package dev.mariany.wellearnedxp;

import dev.mariany.wellearnedxp.attachment.WEXAttachmentTypes;
import dev.mariany.wellearnedxp.packet.WEXPackets;
import dev.mariany.wellearnedxp.particle.WEXParticleTypes;
import dev.mariany.wellearnedxp.registry.WEXDataResources;
import dev.mariany.wellearnedxp.sound.WEXSoundEvents;
import dev.mariany.wellearnedxp.stat.WEXStats;
import dev.mariany.wellearnedxp.stat.handler.CropsHarvestedStatHandler;
import dev.mariany.wellearnedxp.stat.handler.LootDiscoveredStatHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WellEarnedXP implements ModInitializer {
    public static final String MOD_ID = "wellearnedxp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final CropsHarvestedStatHandler CROP_HARVEST_HANDLER = new CropsHarvestedStatHandler();
    public static final LootDiscoveredStatHandler LOOT_DISCOVERED_STAT_HANDLER = new LootDiscoveredStatHandler();

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void bootstrapLog(String type) {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
            return;
        }

        LOGGER.info("Registering {}", type);
    }

    @Override
    public void onInitialize() {
        WEXDataResources.bootstrap();
        WEXAttachmentTypes.bootstrap();
        WEXSoundEvents.bootstrap();
        WEXParticleTypes.bootstrap();
        WEXStats.bootstrap();
        WEXPackets.bootstrap();
        CROP_HARVEST_HANDLER.bootstrap();
        LOOT_DISCOVERED_STAT_HANDLER.bootstrap();
    }
}
