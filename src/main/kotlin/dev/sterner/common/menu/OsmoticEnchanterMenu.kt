package dev.sterner.common.menu

import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import dev.sterner.registry.VoidBoundMenuTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntityInventory

class OsmoticEnchanterMenu(
    i: Int, inventory: Inventory, val pos: BlockPos,
) : AbstractContainerMenu(VoidBoundMenuTypeRegistry.OSMOTIC_ENCHANTER.get(), i) {

    val activated: Boolean = false
    var blockEntityInventory: LodestoneBlockEntityInventory? = null
    var be: OsmoticEnchanterBlockEntity? = null
    var shouldRefresh = true

    constructor(i: Int, inventory: Inventory, buf: FriendlyByteBuf) : this(
        i, inventory, buf.readBlockPos(),
    )

    init {

        if (inventory.player.level().getBlockEntity(pos) is OsmoticEnchanterBlockEntity) {
            be = inventory.player.level().getBlockEntity(pos) as OsmoticEnchanterBlockEntity
            blockEntityInventory = be?.inventory
            this.addSlot(object : Slot(blockEntityInventory!!, 0, 14, 14){

                override fun mayPlace(stack: ItemStack): Boolean {
                    return stack.isEnchantable
                }

                override fun set(stack: ItemStack) {
                    shouldRefresh = true
                    super.set(stack)
                }

                override fun onTake(player: Player, stack: ItemStack) {
                    shouldRefresh = true
                    super.onTake(player, stack)
                }

                override fun mayPickup(player: Player): Boolean {
                    if (be?.progress != 0) {
                        return true
                    }
                    return super.mayPickup(player)
                }
            })
        }

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


    override fun quickMoveStack(playerIn: Player, index: Int): ItemStack {
        var itemstack = ItemStack.EMPTY
        val slot = slots[index]
        if (slot.hasItem()) {
            val itemstack1 = slot.item
            itemstack = itemstack1.copy()
            if (index < this.blockEntityInventory!!.containerSize) {
                if (!this.moveItemStackTo(
                        itemstack1, this.blockEntityInventory!!.getContainerSize(),
                        slots.size, true
                    )
                ) {
                    return ItemStack.EMPTY
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.blockEntityInventory!!.containerSize, false)) {
                return ItemStack.EMPTY
            }

            if (itemstack1.isEmpty) {
                slot.set(ItemStack.EMPTY)
            } else {
                slot.setChanged()
            }
        }

        return itemstack
    }

    override fun stillValid(player: Player): Boolean {
        return true
    }
}