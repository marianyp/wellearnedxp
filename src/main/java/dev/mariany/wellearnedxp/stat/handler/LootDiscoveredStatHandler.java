package dev.mariany.wellearnedxp.stat.handler;

import dev.mariany.wellearnedxp.WellEarnedXP;
import dev.mariany.wellearnedxp.stat.WEXStats;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class LootDiscoveredStatHandler {
    private final @Nullable Stat<?> stat;
    private final @Nullable Identifier customStatId;
    private final BlockBreakHandler decoratedPotBreakHandler;

    public LootDiscoveredStatHandler() {
        this(WEXStats.LOOT_DISCOVERED);
    }

    public LootDiscoveredStatHandler(Identifier customStatId) {
        this(null, customStatId);
    }

    public LootDiscoveredStatHandler(Stat<?> stat) {
        this(stat, null);
    }

    private LootDiscoveredStatHandler(@Nullable Stat<?> stat, @Nullable Identifier customStatId) {
        this.stat = stat;
        this.customStatId = customStatId;
        this.decoratedPotBreakHandler = new BlockBreakHandler(
                LootDiscoveredStatHandler::isLootableDecoratedPot,
                this::awardStat
        );
    }

    public void bootstrap() {
        WellEarnedXP.bootstrapLog("Loot Discovered Stat Handler");
        this.decoratedPotBreakHandler.bootstrap();
    }

    public void onLootTableGenerated(ServerPlayer serverPlayer) {
        this.awardStat(serverPlayer);
    }

    public void onVaultUnlock(Player player) {
        this.awardStat(player);
    }

    public void onTrialSpawnerReward(
            ServerLevel serverLevel,
            @Nullable ResourceKey<LootTable> lootTableResourceKey,
            UUID uuid
    ) {
        if (lootTableResourceKey == null) {
            return;
        }

        Player player = serverLevel.getPlayerByUUID(uuid);

        if (player == null) {
            return;
        }

        this.awardStat(player);
    }

    private void awardStat(Player player) {
        if (this.stat != null) {
            player.awardStat(this.stat);
        } else if (this.customStatId != null) {
            player.awardStat(this.customStatId);
        }
    }

    private static boolean isLootableDecoratedPot(
            Level level,
            Player player,
            BlockPos pos,
            BlockState state,
            @Nullable BlockEntity blockEntity
    ) {
        return blockEntity instanceof DecoratedPotBlockEntity decoratedPot && decoratedPot.getLootTable() != null;
    }
}
