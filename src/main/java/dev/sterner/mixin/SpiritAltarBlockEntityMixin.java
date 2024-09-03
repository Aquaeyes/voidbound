package dev.sterner.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.sammy.malum.common.block.curiosities.spirit_altar.SpiritAltarBlockEntity;
import com.sammy.malum.common.recipe.SpiritInfusionRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpiritAltarBlockEntity.class)
public class SpiritAltarBlockEntityMixin {

    @Shadow public SpiritInfusionRecipe recipe;

    //TODO add this fix to base malum
    @WrapOperation(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setTag(Lnet/minecraft/nbt/CompoundTag;)V"))
    private void spirit_altar_craft(ItemStack instance, CompoundTag compoundTag, Operation<Void> original, @Local(ordinal = 0) ItemStack inputStack) {
        var tag = instance.getTag();
        var oldTag = inputStack.getTag();

        if (tag != null && oldTag != null) {
            var mergedTag = tag.merge(oldTag);
            instance.setTag(mergedTag);
        } else if (tag != null) {
            instance.setTag(tag);
        } else if (oldTag != null) {
            instance.setTag(oldTag);
        }
    }
}
