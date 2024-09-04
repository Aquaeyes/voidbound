package dev.sterner.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.sammy.malum.registry.common.block.BlockRegistry;
import dev.sterner.registry.VoidBoundComponentRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Shadow public abstract void sendSystemMessage(Component component);

    @Inject(method = "onInsideBlock", at = @At("HEAD"))
    private void voidbound$onInsideBlock(BlockState state, CallbackInfo ci) {
        if (state.is(BlockRegistry.PRIMORDIAL_SOUP.get())) {
            ServerPlayer player = (ServerPlayer) (Object) this;
            var comp = VoidBoundComponentRegistry.Companion.getVOID_BOUND_REVELATION_COMPONENT().get(player);
            if (!comp.getHasWellKnowledge()) {
                comp.setHasWellKnowledge(true);
            }
        }
    }

    @Inject(method = "triggerDimensionChangeTriggers", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/advancements/critereon/ChangeDimensionTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceKey;)V"

    ))
    private void voidbound$triggerDimensionChangeTriggers(ServerLevel level, CallbackInfo ci, @Local(ordinal = 0) ResourceKey<Level> resourceKey, @Local(ordinal = 1) ResourceKey<Level> resourceKey2) {

        ServerPlayer player = (ServerPlayer) (Object) this;
        var comp = VoidBoundComponentRegistry.Companion.getVOID_BOUND_REVELATION_COMPONENT().get(player);

        if (resourceKey == Level.OVERWORLD && resourceKey2 == Level.NETHER){
            if (comp.getHasWellKnowledge()) {
                comp.setHasNetherKnowledge(true);
                this.sendSystemMessage(Component.translatable("voidbound.revelation.nether"));
            }
        }
        if (resourceKey == Level.OVERWORLD && resourceKey2 == Level.END){
            if (comp.getHasWellKnowledge()) {
                comp.setHasEndKnowledge(true);
                this.sendSystemMessage(Component.translatable("voidbound.revelation.nether"));
            }
        }
    }
}
