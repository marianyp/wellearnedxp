package dev.mariany.wellearnedxp.engagement;

import dev.mariany.wellearnedxp.attachment.WEXAttachmentTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.UUID;

public interface AwardeeHolder {
    default boolean wellearnedxp$lacksAwardee() {
        return this.wellearnedxp$getAwardee().isEmpty();
    }

    default Optional<UUID> wellearnedxp$getAwardee() {
        if (!(this instanceof Entity entity)) {
            return Optional.empty();
        }

        UUID uuid = entity.getAttached(WEXAttachmentTypes.AWARDEE);

        return Optional.ofNullable(uuid);
    }

    default boolean wellearnedxp$isAwardee(Player player) {
        return this.wellearnedxp$getAwardee().map(uuid -> player.getUUID().equals(uuid)).orElse(false);
    }

    default void wellearnedxp$award(Player awardee) {
        if (!(this instanceof Entity entity)) {
            return;
        }

        entity.setAttached(WEXAttachmentTypes.AWARDEE, awardee.getUUID());
    }
}
