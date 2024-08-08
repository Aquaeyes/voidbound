package dev.sterner.mixin;

import dev.sterner.api.item.HammerLikeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

    @Shadow @Final protected ServerPlayer player;

    @Shadow protected ServerLevel level;

    @Inject(method = "destroyBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z",
                    ordinal = 0, shift = At.Shift.BEFORE)
    )
    public void voidbound$beforeDestroyBlock(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = this.player.getMainHandItem();
        if (itemStack.getItem() instanceof HammerLikeItem hammerLikeItem) {
            BlockState blockState = this.level.getBlockState(blockPos);
            hammerLikeItem.causeAoe(this.level, blockPos, blockState, itemStack, this.player);
        }
    }
}
