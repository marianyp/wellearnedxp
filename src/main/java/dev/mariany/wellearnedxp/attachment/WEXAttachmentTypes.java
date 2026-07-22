package dev.mariany.wellearnedxp.attachment;

import com.mojang.serialization.Codec;
import dev.mariany.wellearnedxp.WellEarnedXP;
import dev.mariany.wellearnedxp.engagement.EngagementTypeData;
import dev.mariany.wellearnedxp.registry.WEXRegistries;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class WEXAttachmentTypes {
    public static final AttachmentType<Map<ResourceKey<EngagementTypeData>, Integer>> ENGAGEMENTS = register(
            "engagements",
            builder -> builder
                    .initializer(HashMap::new)
                    .persistent(Codec.unboundedMap(ResourceKey.codec(WEXRegistries.ENGAGEMENT_TYPE), Codec.INT))
                    .copyOnDeath()
    );

    public static final AttachmentType<Boolean> ENGAGEMENT_REWARD = register(
            "engagement_reward",
            builder -> builder
                    .persistent(Codec.BOOL)
                    .syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
    );

    private WEXAttachmentTypes() {
    }

    public static void bootstrap() {
        WellEarnedXP.bootstrapLog("Attachment Types");
    }

    private static <T> AttachmentType<T> register(
            String name,
            Consumer<AttachmentRegistry.Builder<T>> consumer
    ) {
        return AttachmentRegistry.create(WellEarnedXP.id(name), consumer);
    }
}
