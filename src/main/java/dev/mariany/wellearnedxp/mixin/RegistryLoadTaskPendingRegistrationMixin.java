package dev.mariany.wellearnedxp.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import dev.mariany.wellearnedxp.engagement.type.EngagementTypeData;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.resources.RegistryLoadTask$PendingRegistration")
public class RegistryLoadTaskPendingRegistrationMixin {
    @Redirect(
            method = "loadFromResource",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/Decoder;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"
            )
    )
    private static <T, V> DataResult<T> redirectLoadFromResource(
            Decoder<T> decoder,
            DynamicOps<V> ops,
            V input,
            Decoder<T> elementDecoder,
            RegistryOps<JsonElement> registryOps,
            ResourceKey<T> elementKey,
            Resource resource
    ) {
        return EngagementTypeData.withEntryKey(elementKey, () -> decoder.parse(ops, input));
    }
}
