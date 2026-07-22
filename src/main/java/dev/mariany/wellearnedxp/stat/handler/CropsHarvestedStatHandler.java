package dev.mariany.wellearnedxp.stat.handler;

import dev.mariany.wellearnedxp.WellEarnedXP;
import dev.mariany.wellearnedxp.mixin.accessor.AttachedStemBlockAccessor;
import dev.mariany.wellearnedxp.stat.WEXStats;
import dev.mariany.wellearnedxp.tag.WEXTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;

public class CropsHarvestedStatHandler {
    private final @Nullable Stat<?> stat;
    private final @Nullable Identifier customStatId;
    private final BlockBreakHandler blockBreakHandler;

    public CropsHarvestedStatHandler() {
        this(WEXStats.CROPS_HARVESTED);
    }

    public CropsHarvestedStatHandler(Identifier customStatId) {
        this(null, customStatId);
    }

    public CropsHarvestedStatHandler(Stat<?> stat) {
        this(stat, null);
    }

    private CropsHarvestedStatHandler(@Nullable Stat<?> stat, @Nullable Identifier customStatId) {
        this.stat = stat;
        this.customStatId = customStatId;
        this.blockBreakHandler = new BlockBreakHandler(
                CropsHarvestedStatHandler::isCrop,
                this::awardStat
        );
    }

    public void bootstrap() {
        WellEarnedXP.bootstrapLog("Crops Harvested Stat Handler");
        this.blockBreakHandler.bootstrap();
    }

    private static boolean isCrop(
            Level level,
            Player player,
            BlockPos pos,
            BlockState state,
            @Nullable BlockEntity blockEntity
    ) {
        return isCrop(level, pos, state);
    }

    private static boolean isCrop(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CropBlock cropBlock && cropBlock.isMaxAge(state)) {
            return true;
        }

        if (state.is(WEXTags.Blocks.CROP_HARVESTABLE)) {
            return true;
        }

        if (isAgeCrop(state)) {
            return true;
        }

        return isGourd(level, pos, state);
    }

    private static boolean isAgeCrop(BlockState state) {
        if (!state.is(WEXTags.Blocks.AGE_CROP_HARVESTABLE)) {
            return false;
        }

        return state.getValues().anyMatch(CropsHarvestedStatHandler::isMaxAgePropertyValue);
    }

    private static boolean isMaxAgePropertyValue(Property.Value<?> propertyValue) {
        if (!(propertyValue.value() instanceof Integer integerValue)) {
            return false;
        }

        return getAgeProperty(propertyValue)
                .map(CropsHarvestedStatHandler::getIntegerPropertyMaxValue)
                .map(maxValue -> maxValue <= integerValue)
                .orElse(false);
    }

    private static Optional<IntegerProperty> getAgeProperty(Property.Value<?> propertyValue) {
        if (propertyValue.property() instanceof IntegerProperty integerProperty) {
            if (integerProperty.getName().equals("age")) {
                return Optional.of(integerProperty);
            }
        }

        return Optional.empty();
    }

    private static int getIntegerPropertyMaxValue(IntegerProperty integerProperty) {
        return integerProperty.getPossibleValues().stream().max(Comparator.naturalOrder()).orElse(Integer.MIN_VALUE);
    }

    private static boolean isGourd(Level level, BlockPos pos, BlockState state) {
        for (Direction direction : Direction.values()) {
            BlockState stemState = level.getBlockState(pos.relative(direction));

            if (isValidStem(stemState, state, direction)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isValidStem(BlockState stemState, BlockState state, Direction directionFromGourd) {
        if (!(stemState.getBlock() instanceof AttachedStemBlockAccessor stemAccessor)) {
            return false;
        }

        ResourceKey<Block> targetFruit = stemAccessor.wellearnedxp$fruit();

        if (!state.is(targetFruit) || !stemState.hasProperty(AttachedStemBlock.FACING)) {
            return false;
        }

        return stemState.getValue(AttachedStemBlock.FACING) == directionFromGourd.getOpposite();
    }

    private void awardStat(Player player) {
        if (this.stat != null) {
            player.awardStat(this.stat);
        } else if (this.customStatId != null) {
            player.awardStat(this.customStatId);
        }
    }
}
