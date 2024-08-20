package dev.sterner.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.sammy.malum.common.enchantment.ReboundEnchantment;
import com.sammy.malum.common.entity.boomerang.ScytheBoomerangEntity;
import com.sammy.malum.registry.common.AttributeRegistry;
import dev.sterner.api.IchoriumScytheGhost;
import dev.sterner.common.item.tool.ichor.IchoriumScytheItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ReboundEnchantment.class)
public class ReboundEnchantmentMixin {

    @Inject(method = "onRightClickItem", at = @At(value = "INVOKE", target = "Lcom/sammy/malum/common/entity/boomerang/ScytheBoomerangEntity;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    private static void voidbound$onRightClick(ServerPlayer player, InteractionHand interactionHand, ItemStack stack, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) float baseDamage, @Local(ordinal = 1) float magicDamage) {

        if (stack.getItem() instanceof IchoriumScytheItem) {

            makeScythe(player, stack, baseDamage, magicDamage, 25);
            makeScythe(player, stack, baseDamage, magicDamage, -25);
        }
    }

    @Unique
    private static void makeScythe(ServerPlayer player, ItemStack stack , float baseDamage, float magicDamage, int yOffset){
        ScytheBoomerangEntity entity = new ScytheBoomerangEntity(player.level(), player.position().x, player.position().y + player.getBbHeight() / 2f, player.position().z);
        entity.setData(player, baseDamage, magicDamage,  0);
        entity.setItem(stack);

        ((IchoriumScytheGhost) entity).setGhost(true);

        entity.shootFromRotation(player, player.getXRot(), player.getYRot() + yOffset, 0.0F, (float) (1.5F + player.getAttributeValue(AttributeRegistry.SCYTHE_PROFICIENCY.get()) * 0.125f), 0F);
        player.level().addFreshEntity(entity);
    }
}
