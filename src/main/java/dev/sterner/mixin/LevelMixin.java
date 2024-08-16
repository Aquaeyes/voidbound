package dev.sterner.mixin;

import dev.sterner.registry.VoidBoundComponentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {

    @Inject(method = "destroyBlock", at = @At("RETURN"))
    private void destroyBlock(BlockPos pos, boolean dropBlock, Entity entity, int recursionLeft, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            var level = (Level.class.cast(this));
            var comp = VoidBoundComponentRegistry.Companion.getVOID_BOUND_WORLD_COMPONENT().get(level);
            comp.removePos(GlobalPos.of(level.dimension(), pos));
        }
    }
}
