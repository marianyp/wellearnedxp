package dev.mariany.wellearnedxp.engagement;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public final class CustomStatEngagementType extends EngagementType<Identifier> {
    private static final String NAME_PREFIX = "custom_stat/";

    public CustomStatEngagementType(@NonNull Identifier value, @NonNull Experience experience, int interval) {
        super(value, experience, interval);
    }

    @Override
    public boolean validate(Stat<?> stat) {
        return stat.getType() == Stats.CUSTOM
                && stat.getValue() instanceof Identifier identifier
                && identifier.equals(this.getValue());
    }

    @Override
    public String getName() {
        Optional<String> optionalName = getNameFromRegistry(BuiltInRegistries.CUSTOM_STAT, this.getValue());
        String name = optionalName.orElseGet(this::toNamespacedPath);
        return NAME_PREFIX + name;
    }

    private String toNamespacedPath() {
        return EngagementType.toNamespacedPath(this.getValue());
    }

    public static Builder builder(String rawId) {
        return builder(Identifier.parse(rawId));
    }

    public static Builder builder(@NonNull Identifier customStat) {
        return new Builder(customStat);
    }

    public static final class Builder extends EngagementType.Builder<Identifier, CustomStatEngagementType, Builder> {
        private Builder(@NonNull Identifier customStat) {
            super(customStat);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public CustomStatEngagementType build() {
            return new CustomStatEngagementType(value(), experience(), interval());
        }
    }
}
