package dev.mariany.wellearnedxp.stat;

import dev.mariany.wellearnedxp.WellEarnedXP;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class WEXStats {
    public static final Identifier BLOCKS_PLACED = register("blocks_placed");
    public static final Identifier CROPS_HARVESTED = register("crops_harvested");
    public static final Identifier DIFFERENT_ITEMS_CRAFTED = register("different_items_crafted");
    public static final Identifier LOOT_DISCOVERED = register("loot_discovered");

    private static Identifier register(String id) {
        return register(id, StatFormatter.DEFAULT);
    }

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier location = WellEarnedXP.id(id);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, location, location);
        Stats.CUSTOM.get(location, formatter);
        return location;
    }

    public static void bootstrap() {
        WellEarnedXP.bootstrapLog("Stats");
    }
}
