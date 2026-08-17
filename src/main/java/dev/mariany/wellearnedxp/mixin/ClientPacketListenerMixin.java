package dev.mariany.wellearnedxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.mariany.wellearnedxp.client.WellEarnedXPClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Shadow
    private ClientLevel level;

    @WrapOperation(
            method = "handleTakeItemEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"
            )
    )
    public void wrapHandleTakeItemEntity(
            ClientLevel clientLevel,
            double x,
            double y,
            double z,
            SoundEvent sound,
            SoundSource source,
            float volume,
            float pitch,
            boolean distanceDelay,
            Operation<Void> original,
            @Local(index = 1, argsOnly = true) ClientboundTakeItemEntityPacket packet
    ) {
        Entity from = clientLevel.getEntity(packet.getItemId());
        Entity to = this.level.getEntity(packet.getPlayerId());

        if (to == null) {
            to = Minecraft.getInstance().player;
        }

        if (from != null && WellEarnedXPClient.ENGAGEMENT_REWARD_EFFECTS.playPickupSound(clientLevel, from, to)) {
            return;
        }

        original.call(clientLevel, x, y, z, sound, source, volume, pitch, distanceDelay);
    }
}
