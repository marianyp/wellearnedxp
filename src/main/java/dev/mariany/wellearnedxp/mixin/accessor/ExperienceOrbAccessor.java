package dev.mariany.wellearnedxp.mixin.accessor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ExperienceOrb.class)
public interface ExperienceOrbAccessor {
    @Invoker("tryMergeToExisting")
    static boolean wellearnedxp$tryMergeToExisting(ServerLevel level, Vec3 pos, int value) {
        throw new AssertionError("Mixin transformation failed");
    }

    @Accessor("age")
    void wellearnedxp$setAge(int age);
}
