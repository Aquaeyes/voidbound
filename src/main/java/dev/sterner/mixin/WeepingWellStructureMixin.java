package dev.sterner.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.sammy.malum.common.worldgen.WeepingWellStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(WeepingWellStructure.class)
public class WeepingWellStructureMixin {

    @Inject(method = "findGenerationPoint", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/pools/JigsawPlacement;addPieces(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Lnet/minecraft/core/Holder;Ljava/util/Optional;ILnet/minecraft/core/BlockPos;ZLjava/util/Optional;I)Ljava/util/Optional;"))
    private void void_bound$cacheStructureLoc(Structure.GenerationContext pContext, CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir, @Local(index = 2) BlockPos validPos){

    }
}
