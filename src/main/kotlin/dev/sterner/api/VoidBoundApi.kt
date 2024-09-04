package dev.sterner.api

import com.sammy.malum.client.VoidRevelationHandler
import com.sammy.malum.common.container.WeaversWorkbenchContainer.component
import com.sammy.malum.core.systems.recipe.SpiritWithCount
import dev.sterner.listener.EnchantSpiritDataReloadListener
import dev.sterner.registry.VoidBoundComponentRegistry
import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.Level
import team.lodestar.lodestone.helpers.TrinketsHelper

object VoidBoundApi {

    /**
     * Returns true if a client player has the hallowed goggles or monocle equipped
     */
    fun hasGoggles(): Boolean {
        val player = Minecraft.getInstance().player
        if (player != null) {
            val bl = TrinketsHelper.hasTrinketEquipped(player, VoidBoundItemRegistry.HALLOWED_MONOCLE.get())
            val bl2 = Minecraft.getInstance().player!!.getItemBySlot(EquipmentSlot.HEAD)
                .`is`(
                    VoidBoundItemRegistry.HALLOWED_GOGGLES.get()
                )
            return bl || bl2
        }
        return false
    }

    /**
     * Returns true if a player has the hallowed goggles or monocle equipped
     */
    fun hasGoggles(player: Player): Boolean {
        val bl = TrinketsHelper.hasTrinketEquipped(player, VoidBoundItemRegistry.HALLOWED_MONOCLE.get())
        val bl2 = Minecraft.getInstance().player!!.getItemBySlot(EquipmentSlot.HEAD)
            .`is`(
                VoidBoundItemRegistry.HALLOWED_GOGGLES.get()
            )
        return bl || bl2
    }

    /**
     * Returns how many spirits of each kind a enchantment is worth for the osmotic enchanter
     */
    fun getSpiritFromEnchant(enchantment: Enchantment, level: Int): List<SpiritWithCount> {

        val reg = BuiltInRegistries.ENCHANTMENT.getKey(enchantment)
        val list = EnchantSpiritDataReloadListener.ENCHANTING_DATA[reg]
        val out = mutableListOf<SpiritWithCount>()
        if (list != null) {
            for (spiritIn in list.spirits) {
                out.add(SpiritWithCount(spiritIn.type, spiritIn.count * level))
            }
        }

        return out
    }

    /**
     * Returns false if the block being broken is warded by another player
     */
    fun canPlayerBreakBlock(level: Level, player: Player, blockPos: BlockPos): Boolean {
        val comp = VoidBoundComponentRegistry.VOID_BOUND_WORLD_COMPONENT.get(level)
        if (comp.isEmpty()) {
            return true
        }

        return !comp.isPosBoundToAnotherPlayer(player, GlobalPos.of(player.level().dimension(), blockPos))
    }

    /**
     * Returns false if the block being broken is warded by any player
     */
    fun canBlockBreak(level: Level, blockPos: BlockPos): Boolean {
        val comp = VoidBoundComponentRegistry.VOID_BOUND_WORLD_COMPONENT.get(level)
        if (comp.isEmpty()) {
            return true
        }

        if (comp.hasBlockPos(GlobalPos.of(level.dimension(), blockPos))) {
            return false
        }
        return true
    }

    fun hasTearKnowledgeClient(): Boolean {
        val player = Minecraft.getInstance().player
        if (player != null) {
            val comp = VoidBoundComponentRegistry.VOID_BOUND_REVELATION_COMPONENT.get(player)
            return comp.isTearKnowledgeComplete()
        }
        return false
    }

    fun hasIchorKnowledgeClient(): Boolean {
        val player = Minecraft.getInstance().player
        if (player != null) {
            val comp = VoidBoundComponentRegistry.VOID_BOUND_REVELATION_COMPONENT.get(player)
            return comp.hasIchorKnowledge && comp.hasCrimsonKnowledge && comp.hasWellKnowledge && comp.hasEndKnowledge && comp.hasNetherKnowledge
        }
        return false
    }

    fun hasCrimsonKnowledgeClient(): Boolean {
        val player = Minecraft.getInstance().player
        if (player != null) {
            val comp = VoidBoundComponentRegistry.VOID_BOUND_REVELATION_COMPONENT.get(player)
            return comp.hasCrimsonKnowledge && comp.hasWellKnowledge && comp.hasEndKnowledge && comp.hasNetherKnowledge
        }
        return false
    }

    fun addThought(player: Player, text: Component, duration: Int){
        VoidBoundComponentRegistry.VOID_BOUND_REVELATION_COMPONENT.maybeGet(player).ifPresent {
            it.addThought(text, duration)
        }
    }

    fun hasNetherMessage() : Boolean {
        val component = VoidBoundComponentRegistry.VOID_BOUND_REVELATION_COMPONENT.maybeGet(Minecraft.getInstance().player)
        if (component.isPresent) {
            return component.get().hasReceivedNetherMessage
        }
        return false
    }

    fun hasEndMessage() : Boolean {
        val component = VoidBoundComponentRegistry.VOID_BOUND_REVELATION_COMPONENT.maybeGet(Minecraft.getInstance().player)
        if (component.isPresent) {
            return component.get().hasReceivedEndMessage
        }
        return false
    }
}