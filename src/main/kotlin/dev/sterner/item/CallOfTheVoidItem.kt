package dev.sterner.item

import com.sammy.malum.common.worldgen.WeepingWellStructure
import com.sammy.malum.registry.common.worldgen.StructureRegistry
import dev.sterner.registry.VoidBoundTags
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.levelgen.structure.StructureType
import java.util.*

class CallOfTheVoidItem(properties: Properties) : Item(properties) {


    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {

        if (level is ServerLevel) {
            //MalumMod.malumPath("weeping_well")
            var struct: StructureType<WeepingWellStructure> = StructureRegistry.WEEPING_WELL.get()
            val serverLevel = level as ServerLevel
            val optional = level.registryAccess().registryOrThrow(Registries.STRUCTURE)
            val blockPos = serverLevel.findNearestMapStructure(VoidBoundTags.WEEPING_WELL, entity.onPos, 200, true)
            println("1")
            if (blockPos != null) {
                println("2 : $blockPos")

            }
        }



        super.inventoryTick(stack, level, entity, slotId, isSelected)
    }
}