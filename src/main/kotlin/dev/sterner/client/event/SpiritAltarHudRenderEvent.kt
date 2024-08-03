package dev.sterner.client.event

import com.mojang.authlib.minecraft.client.MinecraftClient
import com.mojang.blaze3d.vertex.PoseStack
import com.sammy.malum.common.block.curiosities.spirit_altar.SpiritAltarBlockEntity
import com.sammy.malum.common.recipe.SpiritInfusionRecipe
import dev.sterner.VoidBound
import dev.sterner.api.utils.RenderUtils
import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.BlockHitResult


object SpiritAltarHudRenderEvent {

    fun spiritAltarRecipeHud(guiGraphics: GuiGraphics, fl: Float) {
        val client: Minecraft = Minecraft.getInstance()
        if (client.player != null && client.player!!.getItemBySlot(EquipmentSlot.HEAD).`is`(VoidBoundItemRegistry.HALLOWED_GOGGLES.get())
        ) {
            if (client.level != null && client.hitResult is BlockHitResult) {
                val result = client.hitResult as BlockHitResult
                val pos: BlockPos = result.blockPos
                if (client.level!!.getBlockEntity(pos) is SpiritAltarBlockEntity) {
                    val se = client.level!!.getBlockEntity(pos) as SpiritAltarBlockEntity
                    val recipes: List<SpiritInfusionRecipe> = se.possibleRecipes
                    if (recipes.isNotEmpty()) {
                        val matrixStack = guiGraphics.pose()
                        matrixStack.pushPose()
                        matrixStack.translate(
                            if (client.window.guiScaledWidth % 2 != 0) 0.5 else 0.0,
                            if (client.window.guiScaledHeight % 2 != 0) 0.5 else 0.0,
                            0.0
                        )
                        var startX: Int = client.window.guiScaledWidth / 2
                        var centerY: Int = client.window.guiScaledHeight / 2
                        startX -= recipes.size / 2 * 16
                        centerY += 10
                        matrixStack.translate(startX.toDouble(), centerY.toDouble(), 0.0)
                        val recipe = recipes[0]
                        val extras = recipe.extraItems
                        val inventory = se.extrasInventory

                        for (extra in extras) {
                            val renderStack = ItemStack(extra.item, extra.getCount())

                            fun isItemInInventory(item: Item): Boolean {
                                for (slot in 0 until inventory.slotCount) {
                                    if (inventory.getItem(slot).item == item) {
                                        return true
                                    }
                                }
                                return false
                            }

                            val checked = isItemInInventory(extra.item)

                            guiGraphics.renderItem(renderStack, 0, 0)
                            if (!checked) {
                                guiGraphics.renderItemDecorations(client.font, renderStack, 0,0)
                            } else {
                                RenderUtils.drawIcon(matrixStack, RenderUtils.CHECKMARK)
                            }
                            matrixStack.translate(16.0, 0.0, 0.0)
                        }
                        matrixStack.popPose()
                    }
                }
            }
        }
    }
}