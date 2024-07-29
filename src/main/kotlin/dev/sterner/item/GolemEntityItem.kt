package dev.sterner.item

import dev.sterner.registry.VoidBoundEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.gameevent.GameEvent

class GolemEntityItem : Item(Properties().stacksTo(1)) {

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        val blockPos = context.clickedPos
        val blockState = level.getBlockState(blockPos)
        val itemStack = context.itemInHand
        val direction = context.clickedFace

        if (level is ServerLevel) {
            val blockPos2: BlockPos = if (blockState.getCollisionShape(level, blockPos).isEmpty) {
                blockPos
            } else {
                blockPos.relative(direction)
            }

            val golemType: EntityType<*> = VoidBoundEntityTypeRegistry.SOUL_STEEL_GOLEM_ENTITY.get()

            if (golemType.spawn(
                    level,
                    itemStack,
                    context.player,
                    blockPos2,
                    MobSpawnType.SPAWN_EGG,
                    true,
                    blockPos != blockPos2 && direction == Direction.UP
                )
                != null
            ) {
                itemStack.shrink(1)
                level.gameEvent(context.player, GameEvent.ENTITY_PLACE, blockPos)
            }
        }

        return super.useOn(context)
    }
}