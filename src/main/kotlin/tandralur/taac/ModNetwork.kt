package tandralur.taac

import net.minecraft.core.GlobalPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel
import tandralur.taac.blocks.entity.SelfRefillingChestEntity
import java.util.function.Supplier

object ModNetwork {
    const val PROTOCOL_VERSION = "1"
    val NETWORK_CHANNEL: SimpleChannel =
        NetworkRegistry.newSimpleChannel(
            ResourceLocation(ArmoryAndCreatures.ID, "self_refilling_chest"),
            { PROTOCOL_VERSION },
            { PROTOCOL_VERSION == it },
            { PROTOCOL_VERSION == it },
        )

    fun registerMessages() {
        var index = 0

        SelfRefillingChestEntity.registerMessages(object : Registration {
            override fun <T> registerMessage(
                cls: Class<T>,
                encoder: (T, FriendlyByteBuf) -> Unit,
                decoder: (FriendlyByteBuf) -> T,
                handler: (T, Supplier<NetworkEvent.Context>) -> Unit
            ) {
                @Suppress("INACCESSIBLE_TYPE")
                NETWORK_CHANNEL.registerMessage(index++, cls, encoder, decoder, handler)
            }
        })
    }

    interface Registration {
        fun <T> registerMessage(
            cls: Class<T>,
            encoder: (T, FriendlyByteBuf) -> Unit,
            decoder: (FriendlyByteBuf) -> T,
            handler: (T, Supplier<NetworkEvent.Context>) -> Unit,
        )
    }

    fun NetworkEvent.Context.getBlockEntity(pos: GlobalPos): BlockEntity? {
        val level = sender?.server?.getLevel(pos.dimension())
        if (level?.isLoaded(pos.pos()) != true) return null
        return level.getBlockEntity(pos.pos())
    }
}