package dev.mariany.wellearnedxp.engagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.mariany.wellearnedxp.registry.WEXRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.StatType;
import net.minecraft.util.ExtraCodecs;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class EngagementTypeData {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngagementTypeData.class);
    private static final ThreadLocal<ResourceKey<?>> ENTRY_KEY = new ThreadLocal<>();

    private static final String STAT_TYPE_PREFIX = "stat_type/";
    private static final String CUSTOM_STAT_PREFIX = "custom_stat/";

    public static final Codec<EngagementTypeData> CODEC = Raw.CODEC.comapFlatMap(
            EngagementTypeData::fromRaw,
            EngagementTypeData::toRaw
    );

    @Nullable
    private final EngagementType<?> engagementType;
    private final Raw raw;

    private EngagementTypeData(@Nullable EngagementType<?> engagementType, Raw raw) {
        this.engagementType = engagementType;
        this.raw = raw;
    }

    public static EngagementTypeData of(EngagementType<?> engagementType) {
        return new EngagementTypeData(
                engagementType,
                new Raw(engagementType.getExperience(), engagementType.getInterval())
        );
    }

    public Optional<EngagementType<?>> engagementType() {
        return Optional.ofNullable(this.engagementType);
    }

    public static <R> DataResult<R> withEntryKey(ResourceKey<?> key, Supplier<DataResult<R>> action) {
        if (!key.isFor(WEXRegistries.ENGAGEMENT_TYPE)) {
            return action.get();
        }

        ENTRY_KEY.set(key);

        try {
            return action.get();
        } finally {
            ENTRY_KEY.remove();
        }
    }

    public static EngagementTypeData empty() {
        return new EngagementTypeData(null, Raw.EMPTY);
    }

    private static DataResult<EngagementTypeData> fromCurrentEntryKey(Raw raw) {
        ResourceKey<?> key = ENTRY_KEY.get();

        if (key == null) {
            if (raw.experience() == null) {
                return DataResult.success(empty());
            }

            return DataResult.error(() -> "Missing engagement type id");
        }

        Identifier id = key.identifier();
        String path = id.getPath();

        if (path.startsWith(STAT_TYPE_PREFIX)) {
            return statTypeFromEntryPath(path.substring(STAT_TYPE_PREFIX.length()), raw);
        }

        if (path.startsWith(CUSTOM_STAT_PREFIX)) {
            return customStatFromEntryPath(path.substring(CUSTOM_STAT_PREFIX.length()), raw);
        }

        return DataResult.error(() -> "Engagement type id must start with stat_type/ or custom_stat/: " + id);
    }

    private static DataResult<EngagementTypeData> statTypeFromEntryPath(String path, Raw raw) {
        return identifierFromEntryPath(path).flatMap(id -> {
            Optional<StatType<?>> statType = BuiltInRegistries.STAT_TYPE.getOptional(id);

            return statType
                    .map(value -> decodeEngagementType(
                                 raw,
                                 experience -> new StatTypeEngagementType(value, experience, raw.interval())
                         )
                    )
                    .orElseGet(() -> skipped("Unknown stat type", id));
        });
    }

    private static DataResult<EngagementTypeData> customStatFromEntryPath(String path, Raw raw) {
        return identifierFromEntryPath(path).flatMap(id -> {
            if (!BuiltInRegistries.CUSTOM_STAT.containsKey(id)) {
                return skipped("Unknown custom stat", id);
            }

            return decodeEngagementType(
                    raw,
                    experience -> new CustomStatEngagementType(id, experience, raw.interval())
            );
        });
    }

    private static DataResult<EngagementTypeData> decodeEngagementType(
            Raw raw,
            Function<EngagementType.Experience, EngagementType<?>> factory
    ) {
        EngagementType.Experience experience = raw.experience();

        if (experience == null) {
            return DataResult.success(empty());
        }

        return DataResult.success(of(factory.apply(experience)));
    }

    private static DataResult<EngagementTypeData> skipped(String reason, Identifier id) {
        LOGGER.warn("{}: {}; skipping engagement type", reason, id);
        return DataResult.success(empty());
    }

    private static DataResult<Identifier> identifierFromEntryPath(String path) {
        int namespaceEnd = path.indexOf('/');

        if (namespaceEnd <= 0 || namespaceEnd == path.length() - 1) {
            return DataResult.error(() -> "Expected namespaced path like namespace/path, got: " + path);
        }

        return Identifier.read(path.substring(0, namespaceEnd) + ":" + path.substring(namespaceEnd + 1));
    }

    private static DataResult<EngagementTypeData> fromRaw(Raw raw) {
        return fromCurrentEntryKey(raw);
    }

    private static Raw toRaw(EngagementTypeData data) {
        return data.raw;
    }

    private record Raw(EngagementType.@Nullable Experience experience, int interval) {
        public static final Raw EMPTY = new Raw(null, EngagementType.DEFAULT_INTERVAL);

        public static final Codec<Raw> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EngagementType.Experience.CODEC
                                .optionalFieldOf("experience")
                                .forGetter(raw -> Optional.ofNullable(raw.experience())),
                        ExtraCodecs.POSITIVE_INT
                                .optionalFieldOf("interval", EngagementType.DEFAULT_INTERVAL)
                                .forGetter(Raw::interval)
                ).apply(
                        instance,
                        (optionalExperience, interval) -> new Raw(
                                optionalExperience.orElse(null),
                                interval
                        )
                )
        );
    }
}
