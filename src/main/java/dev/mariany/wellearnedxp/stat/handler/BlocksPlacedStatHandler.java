package dev.mariany.wellearnedxp.stat.handler;

import dev.mariany.wellearnedxp.stat.WEXStats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;

public class BlocksPlacedStatHandler {
    private BlocksPlacedStatHandler() {
    }

    public static void onBlockPlace(BlockPlaceContext blockPlaceContext) {
        Player player = blockPlaceContext.getPlayer();

        if (player == null) {
            return;
        }

        player.awardStat(WEXStats.BLOCKS_PLACED);
    }
}
