package dev.mariany.wellearnedxp.packet;

import dev.mariany.wellearnedxp.WellEarnedXP;
import dev.mariany.wellearnedxp.packet.clientbound.PlayerRewardedPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;

public final class WEXPackets {
    private WEXPackets() {}

    public static void bootstrap() {
        WellEarnedXP.bootstrapLog("Packets");
        clientBound(PayloadTypeRegistry.clientboundPlay());
        serverBound(PayloadTypeRegistry.serverboundPlay());
    }

    private static void clientBound(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry) {
        registry.register(PlayerRewardedPayload.ID, PlayerRewardedPayload.STREAM_CODEC);
    }

    private static void serverBound(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry) {
    }
}
