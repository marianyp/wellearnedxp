package dev.mariany.wellearnedxp.packet.clientbound;

import dev.mariany.wellearnedxp.WellEarnedXP;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record PlayerRewardedPayload(UUID uuid) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerRewardedPayload> ID = new CustomPacketPayload.Type<>(
            WellEarnedXP.id("update_tiredness_logic")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerRewardedPayload> STREAM_CODEC = StreamCodec
            .composite(
                    UUIDUtil.STREAM_CODEC,
                    PlayerRewardedPayload::uuid,
                    PlayerRewardedPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
