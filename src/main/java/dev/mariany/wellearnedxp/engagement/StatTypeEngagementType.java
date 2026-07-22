package dev.mariany.wellearnedxp.engagement;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import org.jspecify.annotations.NonNull;

public final class StatTypeEngagementType extends EngagementType<StatType<?>> {
    private static final String NAME_PREFIX = "stat_type/";

    public StatTypeEngagementType(@NonNull StatType<?> value, @NonNull Experience experience, int interval) {
        super(value, experience, interval);
    }

    @Override
    public boolean validate(Stat<?> stat) {
        return this.getValue().equals(stat.getType());
    }

    @Override
    public String getName() {
        return NAME_PREFIX + getNameFromRegistry(BuiltInRegistries.STAT_TYPE, this.getValue()).orElseThrow();
    }

    public static Builder builder(@NonNull StatType<?> statType) {
        return new Builder(statType);
    }

    public static final class Builder extends EngagementType.Builder<StatType<?>, StatTypeEngagementType, Builder> {
        private Builder(@NonNull StatType<?> statType) {
            super(statType);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public StatTypeEngagementType build() {
            return new StatTypeEngagementType(value(), experience(), interval());
        }
    }
}
