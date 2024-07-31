package dev.sterner.mixin;

import com.sammy.malum.client.screen.codex.objects.progression.ProgressionEntryObject;
import dev.sterner.api.ProgressionEntryObjectExtension;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ProgressionEntryObject.class)
public class ProgressionEntryObjectMixin implements ProgressionEntryObjectExtension {

    @Shadow public ItemStack iconStack;

    @NotNull
    @Override
    public ProgressionEntryObject setIcon(@NotNull ItemStack itemStack) {
        iconStack = itemStack;
        return (ProgressionEntryObject)(Object)this;
    }
}
