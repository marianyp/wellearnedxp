package dev.mariany.wellearnedxp.engagement;

import dev.mariany.wellearnedxp.registry.WEXDataResources;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class EngagementTypeDataLookup {
    private EngagementTypeDataLookup() {
    }

    public static Stream<Entry> matching(ServerPlayer serverPlayer, Stat<?> stat) {
        return matching(serverPlayer.level(), stat);
    }

    private static Stream<Entry> matching(ServerLevel serverLevel, Stat<?> stat) {
        return matching(serverLevel.getServer(), stat);
    }

    private static Stream<Entry> matching(MinecraftServer server, Stat<?> stat) {
        return matching(server.getOrThrow(WEXDataResources.ENGAGEMENT_TYPES), stat);
    }

    private static Stream<Entry> matching(Map<ResourceKey<EngagementTypeData>, EngagementTypeData> engagementTypes, Stat<?> stat) {
        return engagementTypes
                .entrySet()
                .stream()
                .flatMap(entry -> toEntry(entry, stat).stream());
    }

    private static Optional<Entry> toEntry(Map.Entry<ResourceKey<EngagementTypeData>, EngagementTypeData> entry, Stat<?> stat) {
        return entry
                .getValue()
                .engagementType()
                .filter(engagementType -> engagementType.validate(stat))
                .map(engagementType -> new Entry(entry.getKey(), engagementType));
    }

    public record Entry(ResourceKey<EngagementTypeData> key, EngagementType<?> engagementType) {
    }
}
