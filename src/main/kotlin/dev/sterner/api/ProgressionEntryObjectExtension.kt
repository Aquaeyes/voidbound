package dev.sterner.api

import com.sammy.malum.client.screen.codex.objects.progression.ProgressionEntryObject
import net.minecraft.world.item.ItemStack

interface ProgressionEntryObjectExtension {
    fun setIcon(itemStack: ItemStack): ProgressionEntryObject
}