package dev.mariany.wellearnedxp.packet.clientbound;

import dev.mariany.wellearnedxp.WellEarnedXP;
import dev.mariany.wellearnedxp.client.WellEarnedXPClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

public final class ClientBoundPackets {
    private ClientBoundPackets() {
    }

    public static void bootstrap() {
        WellEarnedXP.bootstrapLog("Client Bound Packets");
        ClientPlayNetworking.registerGlobalReceiver(PlayerRewardedPayload.ID, ClientBoundPackets::onPlayerRewarded);
    }

    private static void onPlayerRewarded(PlayerRewardedPayload payload, ClientPlayNetworking.Context context) {
        Minecraft client = context.client();
        ClientLevel clientLevel = client.level;

        if (clientLevel == null) {
            return;
        }

        Player player = clientLevel.getPlayerByUUID(payload.uuid());

        if (player == null) {
            return;
        }

        WellEarnedXPClient.ENGAGEMENT_REWARD_EFFECTS.onPlayerRewarded(player);
    }
}
