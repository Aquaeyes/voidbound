package dev.sterner.registry

import com.sammy.malum.registry.common.PacketRegistry.MALUM_CHANNEL
import dev.sterner.VoidBound
import dev.sterner.networking.SpiritBinderParticlePacket
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor
import me.pepperbell.simplenetworking.SimpleChannel
import net.fabricmc.api.EnvType


object VoidBoundPacketRegistry {

    const val PROTOCOL_VERSION: String = "1"
    val VOIDBOUND_CHANNEL: SimpleChannel = SimpleChannel(VoidBound.id("main"))

    init {

    }

    fun registerVoidBoundPackets() {
        VOIDBOUND_CHANNEL.initServerListener()
        EnvExecutor.runWhenOn(
            EnvType.CLIENT
        ) { Runnable { VOIDBOUND_CHANNEL.initClientListener() } }

        var index = 0
        VOIDBOUND_CHANNEL.registerS2CPacket(
            SpiritBinderParticlePacket::class.java, index++
        )
    }

}