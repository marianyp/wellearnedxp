package dev.mariany.wellearnedxp.stat.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class BlockBreakHandler {
    private final ShouldQueueBlockBreak shouldQueueBlockBreak;
    private final Consumer<Player> onBlockBreak;
    private final List<PendingBlockBreak> pendingBlockBreaks = new ArrayList<>();

    public BlockBreakHandler(ShouldQueueBlockBreak shouldQueueBlockBreak, Consumer<Player> onBlockBreak) {
        this.shouldQueueBlockBreak = shouldQueueBlockBreak;
        this.onBlockBreak = onBlockBreak;
    }

    public void bootstrap() {
        PlayerBlockBreakEvents.BEFORE.register(this::beforeBlockBreak);
        PlayerBlockBreakEvents.AFTER.register(this::afterBlockBreak);
        PlayerBlockBreakEvents.CANCELED.register(this::onBlockBreakCanceled);
        ServerTickEvents.END_SERVER_TICK.register(this::onServerTick);
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);
    }

    private boolean beforeBlockBreak(
            Level level,
            Player player,
            BlockPos pos,
            BlockState state,
            @Nullable BlockEntity blockEntity
    ) {
        if (!level.isClientSide()) {
            this.queueBlockBreakIfApplicable(level, player, pos, state, blockEntity);
        }

        return true;
    }

    private void queueBlockBreakIfApplicable(
            Level level,
            Player player,
            BlockPos pos,
            BlockState state,
            @Nullable BlockEntity blockEntity
    ) {
        if (this.shouldQueueBlockBreak.test(level, player, pos, state, blockEntity)) {
            this.pendingBlockBreaks.add(PendingBlockBreak.forBlockBreak(level, player, pos));
        } else {
            this.removePendingBlockBreaks(level, player, pos);
        }
    }

    private void afterBlockBreak(
            Level level,
            Player player,
            BlockPos pos,
            BlockState state,
            @Nullable BlockEntity blockEntity
    ) {
        if (level.isClientSide()) {
            return;
        }

        this.runCallbackIfPending(level, player, pos);
    }

    private void runCallbackIfPending(Level level, Player player, BlockPos pos) {
        if (!this.removePendingBlockBreaks(level, player, pos)) {
            return;
        }

        this.onBlockBreak.accept(player);
    }

    private void onBlockBreakCanceled(
            Level level,
            Player player,
            BlockPos pos,
            BlockState state,
            @Nullable BlockEntity blockEntity
    ) {
        if (level.isClientSide()) {
            return;
        }

        this.removePendingBlockBreaks(level, player, pos);
    }

    private void onServerTick(MinecraftServer server) {
        this.pendingBlockBreaks.clear();
    }

    private boolean removePendingBlockBreaks(Level level, Player player, BlockPos pos) {
        return this.pendingBlockBreaks.removeIf(pendingBlockBreak -> pendingBlockBreak.matches(
                level,
                player,
                pos
        ));
    }

    private void onServerStopped(MinecraftServer server) {
        this.resetPendingBlockBreaks();
    }

    private void resetPendingBlockBreaks() {
        this.pendingBlockBreaks.clear();
    }

    @FunctionalInterface
    public interface ShouldQueueBlockBreak {
        boolean test(
                Level level,
                Player player,
                BlockPos pos,
                BlockState state,
                @Nullable BlockEntity blockEntity
        );
    }

    private record PendingBlockBreak(UUID playerId, ResourceKey<Level> dimension, long pos) {
        private static PendingBlockBreak forBlockBreak(Level level, Player player, BlockPos pos) {
            return new PendingBlockBreak(player.getUUID(), level.dimension(), pos.asLong());
        }

        private boolean matches(Level level, Player player, BlockPos pos) {
            return this.playerId.equals(player.getUUID())
                    && this.dimension.equals(level.dimension())
                    && this.pos == pos.asLong();
        }
    }
}
