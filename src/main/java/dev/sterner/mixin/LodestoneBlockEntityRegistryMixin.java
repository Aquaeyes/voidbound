package dev.sterner.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.sterner.common.block.EldritchObeliskComponentBlock;
import dev.sterner.registry.VoidBoundBlockRegistry;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import team.lodestar.lodestone.registry.common.LodestoneBlockEntityRegistry;

/**
 * This is so sad, lodestones multiblock api is on life support
 */
@Mixin(LodestoneBlockEntityRegistry.class)
public class LodestoneBlockEntityRegistryMixin {

    @ModifyReturnValue(method = "getBlocks", at = @At("RETURN"))
    private static Block[] getBlocks(Block[] original) {

        Block[] modified = new Block[original.length + 1];

        System.arraycopy(original, 0, modified, 0, original.length);

        Block myNewBlock = VoidBoundBlockRegistry.INSTANCE.getELDRITCH_OBELISK_COMPONENT().get();
        modified[original.length] = myNewBlock;

        return modified;
    }
}
