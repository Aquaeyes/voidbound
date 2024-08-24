package dev.sterner.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.sterner.api.VoidBoundApi;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderMan.EndermanTakeBlockGoal.class)
public class EndermanTakeBlockGoalMixin {

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean voidbound$tick(boolean original, @Local Level level, @Local BlockPos blockPos) {
        if (!VoidBoundApi.INSTANCE.canBlockBreak(level, blockPos)) {
            return false;
        }
        return original;
    }
}