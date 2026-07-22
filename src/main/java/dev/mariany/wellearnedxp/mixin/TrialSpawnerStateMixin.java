package dev.mariany.wellearnedxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.mariany.wellearnedxp.WellEarnedXP;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;
import java.util.UUID;

@Mixin(TrialSpawnerState.class)
public class TrialSpawnerStateMixin {
    @WrapOperation(
            method = "tickAndGetNext",
            at = @At(value = "INVOKE", target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z")
    )
    boolean wrapTickAndGetNext(
            Set<UUID> instance,
            Object object,
            Operation<Boolean> original,
            @Local(index = 2, argsOnly = true) TrialSpawner trialSpawner,
            @Local(index = 3, argsOnly = true) ServerLevel serverLevel
    ) {
        if (object instanceof UUID uuid) {
            @Nullable ResourceKey<LootTable> lootTableResourceKey = trialSpawner
                    .getStateData()
                    .pack()
                    .ejectingLootTable()
                    .orElse(null);

            WellEarnedXP.LOOT_DISCOVERED_STAT_HANDLER.onTrialSpawnerReward(serverLevel, lootTableResourceKey, uuid);
        }

        return original.call(instance, object);
    }
}
