package dev.mariany.wellearnedxp.engagement;

import dev.mariany.wellearnedxp.attachment.WEXAttachmentTypes;
import net.minecraft.world.entity.Entity;

public interface EngagementRewardCandidate {
    default boolean wellearnedxp$isEngagementReward() {
        if (!(this instanceof Entity entity)) {
            return false;
        }

        return entity.getAttachedOrElse(WEXAttachmentTypes.ENGAGEMENT_REWARD, false);
    }

    default void wellearnedxp$markAsEngagementReward() {
        if (!(this instanceof Entity entity)) {
            return;
        }

        entity.setAttached(WEXAttachmentTypes.ENGAGEMENT_REWARD, true);
    }
}
