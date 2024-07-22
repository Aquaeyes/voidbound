package dev.sterner

import com.sammy.malum.MalumMod
import dev.sterner.client.SpiritBinderBlockEntityRenderer
import dev.sterner.registry.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import org.slf4j.LoggerFactory

object VoidBound : ModInitializer, ClientModInitializer {
	val modid: String = "voidbound"
    private val logger = LoggerFactory.getLogger(modid)


	override fun onInitialize() {

		VoidBoundItemRegistry.ITEMS.register()
		VoidBoundBlockRegistry.BLOCKS.register()
		VoidBoundBlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register()

		VoidBoundCreativeTabRegistry.init()
	}

	override fun onInitializeClient() {


		VoidBoundShaders.init()

		BlockEntityRenderers.register(VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER.get(), ::SpiritBinderBlockEntityRenderer)
	}
}