package dev.sterner.common.menu

import dev.sterner.registry.VoidBoundMenuTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class OsmoticEnchanterMenu(
    i: Int, inventory: Inventory, val pos: BlockPos,
) : AbstractContainerMenu(VoidBoundMenuTypeRegistry.OSMOTIC_ENCHANTER.get(), i) {

    constructor(i: Int, inventory: Inventory, buf: FriendlyByteBuf) : this(
        i, inventory, buf.readBlockPos(),
    )

    var container: Container = SimpleContainer(1)



    init {
        checkContainerSize(container, 1)
        container.startOpen(inventory.player)

        this.addSlot(Slot(container, 0, 14, 14))

        var m: Int

        var l = 0
        while (l < 3) {
            m = 0
            while (m < 9) {
                this.addSlot(Slot(inventory, m + l * 9 + 9, 36 + m * 18, 3 * 18 + 86 + l * 18))
                ++m
            }
            ++l
        }

        l = 0
        while (l < 9) {
            this.addSlot(Slot(inventory, l, 36 + l * 18, 3 * 18 + 144))
            ++l
        }
    }

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun stillValid(player: Player): Boolean {
        return container.stillValid(player)
    }
}