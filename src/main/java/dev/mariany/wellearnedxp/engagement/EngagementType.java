package dev.mariany.wellearnedxp.engagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.util.ExtraCodecs;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Optional;

public abstract class EngagementType<T> {
    protected static final int DEFAULT_INTERVAL = 1;

    private final T value;
    private final Experience experience;
    private final int interval;

    protected EngagementType(@NonNull T value, @NonNull Experience experience, int interval) {
        this.value = Objects.requireNonNull(value, "value must not be null");
        this.experience = Objects.requireNonNull(experience, "experience must not be null");
        this.interval = interval;
    }

    protected static <T> Optional<String> getNameFromRegistry(Registry<T> registry, T value) {
        return Optional.ofNullable(registry.getKey(value)).map(EngagementType::toNamespacedPath);
    }

    protected static String toNamespacedPath(Identifier id) {
        return id.getNamespace() + "/" + id.getPath();
    }

    public abstract boolean validate(Stat<?> stat);

    public abstract String getName();

    public T getValue() {
        return this.value;
    }

    public Experience getExperience() {
        return this.experience;
    }

    public int getInterval() {
        return this.interval;
    }

    public record Experience(int min, int max) {
        private record Raw(int min, int max) {
            private static final Codec<Raw> CODEC = RecordCodecBuilder.create(
                    instance -> instance.group(
                            ExtraCodecs.NON_NEGATIVE_INT
                                    .fieldOf("min")
                                    .forGetter(Raw::min),
                            ExtraCodecs.NON_NEGATIVE_INT
                                    .fieldOf("max")
                                    .forGetter(Raw::max)
                    ).apply(instance, Raw::new)
            );
        }

        public static final Codec<Experience> CODEC = Raw.CODEC.comapFlatMap(
                Experience::fromRaw,
                experience -> new Raw(experience.min(), experience.max())
        );

        private static DataResult<Experience> fromRaw(Raw raw) {
            if (raw.min() > raw.max()) {
                return DataResult.error(() -> "'min' must be less than or equal to 'max'; got min="
                        + raw.min()
                        + ", max="
                        + raw.max()
                );
            }

            return DataResult.success(new Experience(raw.min(), raw.max()));
        }

        public Experience {
            if (min > max) {
                throw new IllegalArgumentException(
                        "min must be less than or equal to max: min=" + min + ", max=" + max
                );
            }
        }
    }

    public abstract static class Builder<T, E extends EngagementType<T>, B extends Builder<T, E, B>> {
        private final T value;
        private Experience experience;
        private int interval = DEFAULT_INTERVAL;

        protected Builder(@NonNull T value) {
            this.value = Objects.requireNonNull(value, "value must not be null");
        }

        public B experience(@NonNull Experience experience) {
            this.experience = Objects.requireNonNull(experience, "experience must not be null");
            return self();
        }

        public B experience(int min, int max) {
            return this.experience(new Experience(min, max));
        }

        public B experience(int value) {
            return this.experience(new Experience(value, value));
        }

        public B interval(int interval) {
            this.interval = interval;
            return self();
        }

        protected T value() {
            return this.value;
        }

        protected Experience experience() {
            return this.experience;
        }

        protected int interval() {
            return this.interval;
        }

        protected abstract B self();

        public abstract E build();
    }
}
