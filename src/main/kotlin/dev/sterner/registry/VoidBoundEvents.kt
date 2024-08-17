package dev.sterner.registry

import com.sammy.malum.common.events.MalumCodexEvents
import dev.sterner.api.ClientTickHandler
import dev.sterner.client.event.MalumCodexEvent
import dev.sterner.client.event.SpiritAltarHudRenderEvent
import dev.sterner.common.components.VoidBoundPlayerComponent
import dev.sterner.common.components.VoidBoundWorldComponent
import dev.sterner.common.item.tool.AxeOfTheStreamItem
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback

object VoidBoundEvents {

    fun init() {
        UseBlockCallback.EVENT.register(VoidBoundPlayerComponent.Companion::useBlock)
        UseEntityCallback.EVENT.register(VoidBoundPlayerComponent.Companion::useEntity)
        BlockEvents.BLOCK_BREAK.register(VoidBoundWorldComponent.Companion::removeWard)
        BlockEvents.BLOCK_BREAK.register(AxeOfTheStreamItem.Companion::breakBlock)
    }

    @Environment(EnvType.CLIENT)
    fun clientInit() {
        MalumCodexEvents.EVENT.register(MalumCodexEvent::addVoidBoundEntries)
        MalumCodexEvents.VOID_EVENT.register(MalumCodexEvent::addVoidBoundVoidEntries)
        WorldRenderEvents.AFTER_TRANSLUCENT.register(VoidBoundPlayerComponent.Companion::renderCubeAtPos)
        WorldRenderEvents.AFTER_TRANSLUCENT.register(VoidBoundWorldComponent.Companion::renderCubeAtPos)
        HudRenderCallback.EVENT.register(SpiritAltarHudRenderEvent::spiritAltarRecipeHud)
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickHandler::clientTickEnd)
    }
}