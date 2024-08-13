package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.networking.*
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor
import me.pepperbell.simplenetworking.SimpleChannel
import net.fabricmc.api.EnvType

object VoidBoundPacketRegistry {

    const val PROTOCOL_VERSION: String = "1"
    val VOIDBOUND_CHANNEL: SimpleChannel = SimpleChannel(VoidBound.id("main"))

    fun registerVoidBoundPackets() {
        VOIDBOUND_CHANNEL.initServerListener()
        EnvExecutor.runWhenOn(
            EnvType.CLIENT
        ) { Runnable { VOIDBOUND_CHANNEL.initClientListener() } }

        var index = 0
        VOIDBOUND_CHANNEL.registerS2CPacket(
            SpiritBinderParticlePacket::class.java, index++
        )

        VOIDBOUND_CHANNEL.registerS2CPacket(
            HeartParticlePacket::class.java, index++
        )
        VOIDBOUND_CHANNEL.registerS2CPacket(
            BubbleParticlePacket::class.java, index++
        )

        //C2S
        VOIDBOUND_CHANNEL.registerC2SPacket(
            EnchantmentLevelPacket::class.java, index++
        )
        VOIDBOUND_CHANNEL.registerC2SPacket(
            StartEnchantingPacket::class.java, index++
        )
        VOIDBOUND_CHANNEL.registerC2SPacket(
            RemoveEnchantPacket::class.java, index++
        )
    }

}