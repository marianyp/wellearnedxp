package dev.mariany.wellearnedxp.stat.handler;

import dev.mariany.wellearnedxp.stat.WEXStats;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class DifferentItemsCraftedStatHandler {
    private DifferentItemsCraftedStatHandler() {
    }

    public static void beforeStatIncrement(Player player, Stat<?> stat) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        beforeStatIncrement(serverPlayer, stat);
    }

    private static void beforeStatIncrement(ServerPlayer serverPlayer, Stat<?> stat) {
        if (!stat.getType().equals(Stats.ITEM_CRAFTED)) {
            return;
        }

        if (!(stat.getValue() instanceof ItemLike itemLike) || itemLike.asItem().equals(Items.AIR)) {
            return;
        }

        int count = getStatCount(serverPlayer, stat);

        if (count > 0) {
            return;
        }

        serverPlayer.awardStat(WEXStats.DIFFERENT_ITEMS_CRAFTED);
    }

    private static int getStatCount(ServerPlayer serverPlayer, Stat<?> stat) {
        return serverPlayer.getStats().getValue(stat);
    }
}
