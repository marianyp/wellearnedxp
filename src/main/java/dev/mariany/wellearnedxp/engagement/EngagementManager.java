package dev.mariany.wellearnedxp.engagement;

import dev.mariany.wellearnedxp.attachment.WEXAttachmentTypes;
import dev.mariany.wellearnedxp.mixin.accessor.ExperienceOrbAccessor;
import dev.mariany.wellearnedxp.packet.clientbound.PlayerRewardedPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class EngagementManager {
    private static final int EXPERIENCE_ORB_MAX_AGE_TICKS = 6000;
    private static final int EXPERIENCE_ORB_LIFESPAN_TICKS = 20;
    private static final int EXPERIENCE_ORB_AGE_TICKS = EXPERIENCE_ORB_MAX_AGE_TICKS - EXPERIENCE_ORB_LIFESPAN_TICKS;

    public static void afterStatIncrement(Player player, Stat<?> stat) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        afterStatIncrement(serverPlayer, stat);
    }

    private static void afterStatIncrement(ServerPlayer serverPlayer, Stat<?> stat) {
        EngagementTypeDataLookup.matching(serverPlayer, stat).forEach(entry -> onEngagement(serverPlayer, entry));
    }

    private static void onEngagement(ServerPlayer serverPlayer, EngagementTypeDataLookup.Entry entry) {
        if (!engage(serverPlayer, entry)) {
            return;
        }

        rewardPlayer(serverPlayer, entry.engagementType());
    }

    private static boolean engage(ServerPlayer serverPlayer, EngagementTypeDataLookup.Entry entry) {
        return engage(
                serverPlayer,
                entry.key(),
                entry.engagementType().getInterval()
        );
    }

    private static boolean engage(
            ServerPlayer serverPlayer,
            ResourceKey<EngagementTypeData> engagementTypeDataKey,
            int interval
    ) {
        final Map<ResourceKey<EngagementTypeData>, Integer> engagements = new HashMap<>(getEngagements(serverPlayer));

        final int currentCount = engagements.getOrDefault(engagementTypeDataKey, 0);
        final int nextCount = currentCount >= interval ? 1 : currentCount + 1;

        engagements.put(engagementTypeDataKey, nextCount);
        serverPlayer.setAttached(WEXAttachmentTypes.ENGAGEMENTS, engagements);

        return nextCount >= interval;
    }

    private static Map<ResourceKey<EngagementTypeData>, Integer> getEngagements(ServerPlayer serverPlayer) {
        return serverPlayer.getAttachedOrCreate(WEXAttachmentTypes.ENGAGEMENTS);
    }

    private static void rewardPlayer(ServerPlayer serverPlayer, EngagementType<?> engagementType) {
        rewardPlayer(serverPlayer, engagementType.getExperience());
    }

    private static void rewardPlayer(ServerPlayer serverPlayer, EngagementType.Experience experience) {
        rewardPlayer(serverPlayer, experience.min(), experience.max());
    }

    private static void rewardPlayer(ServerPlayer serverPlayer, int minExperience, int maxExperience) {
        int xpReward = Mth.nextInt(serverPlayer.getRandom(), minExperience, maxExperience);
        spawnReward(serverPlayer, xpReward);
    }

    private static void spawnReward(ServerPlayer serverPlayer, int amount) {
        if (amount <= 0) {
            return;
        }

        spawnReward(serverPlayer.level(), serverPlayer.position(), amount);
        notifyAllOfReward(serverPlayer);
    }

    private static void spawnReward(ServerLevel level, Vec3 pos, int amount) {
        while (amount > 0) {
            int experienceValue = Math.min(amount, Short.MAX_VALUE);

            amount -= experienceValue;

            if (ExperienceOrbAccessor.wellearnedxp$tryMergeToExisting(level, pos, experienceValue)) {
                continue;
            }

            ExperienceOrb experienceOrb = new ExperienceOrb(level, pos, Vec3.ZERO, experienceValue);

            ((ExperienceOrbAccessor) experienceOrb).wellearnedxp$setAge(EXPERIENCE_ORB_AGE_TICKS);

            if (experienceOrb instanceof EngagementRewardCandidate engagementRewardCandidate) {
                engagementRewardCandidate.wellearnedxp$markAsEngagementReward();
            }

            level.addFreshEntity(experienceOrb);
        }
    }

    private static void notifyAllOfReward(ServerPlayer trackedPlayer) {
        UUID uuid = trackedPlayer.getUUID();
        Set<ServerPlayer> serverPlayers = getTrackingPlayersIncludingTarget(trackedPlayer);
        serverPlayers.forEach(serverPlayer -> notifyPlayerOfReward(serverPlayer, uuid));
    }

    private static Set<ServerPlayer> getTrackingPlayersIncludingTarget(ServerPlayer trackedPlayer) {
        Set<ServerPlayer> recipients = new HashSet<>(PlayerLookup.tracking(trackedPlayer));
        recipients.add(trackedPlayer);
        return recipients;
    }

    private static void notifyPlayerOfReward(ServerPlayer serverPlayer, UUID rewardedPlayerUuid) {
        ServerPlayNetworking.send(serverPlayer, new PlayerRewardedPayload(rewardedPlayerUuid));
    }
}
