package tandralur.taac.blocks.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.ChestBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.network.NetworkEvent
import tandralur.taac.ModNetwork
import tandralur.taac.ModNetwork.getBlockEntity
import tandralur.taac.inWholeTicks
import java.util.function.Supplier
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


class SelfRefillingChestEntity(pos: BlockPos, state: BlockState) :
    ChestBlockEntity(ModBlockEntities.SELF_REFILLING_CHEST.get(), pos, state) {

    companion object {
        fun registerMessages(registration: ModNetwork.Registration) {
            registration.registerMessage(
                CopyInventoryMessage::class.java,
                CopyInventoryMessage::encode,
                CopyInventoryMessage.Companion::decode,
                CopyInventoryMessage::handle
            )
            registration.registerMessage(
                SetRefillIntervalMessage::class.java,
                SetRefillIntervalMessage::encode,
                SetRefillIntervalMessage.Companion::decode,
                SetRefillIntervalMessage::handle
            )
        }

        const val TAG_REFILL = "RefillInventory"
        const val TAG_INTERVAL = "RefillIntervalSeconds"
        const val TAG_LAST_REFILL = "RefillLastTime"

        data class CopyInventoryMessage(val pos: GlobalPos) {
            companion object {
                fun decode(buffer: FriendlyByteBuf) = CopyInventoryMessage(buffer.readGlobalPos())
            }

            fun encode(buffer: FriendlyByteBuf) {
                buffer.writeGlobalPos(pos)
            }

            fun handle(context: Supplier<NetworkEvent.Context>) {
                val ctx = context.get()
                ctx.enqueueWork {
                    (ctx.getBlockEntity(pos) as? SelfRefillingChestEntity)?.copyChestInventoryToRefill()
                }
                ctx.packetHandled = true
            }
        }

        data class SetRefillIntervalMessage(val pos: GlobalPos, val refillIntervalSeconds: Long) {
            companion object {
                fun decode(buffer: FriendlyByteBuf) =
                    SetRefillIntervalMessage(buffer.readGlobalPos(), buffer.readVarLong())
            }

            fun encode(buffer: FriendlyByteBuf) {
                buffer.writeGlobalPos(pos)
                buffer.writeVarLong(refillIntervalSeconds)
            }

            fun handle(context: Supplier<NetworkEvent.Context>) {
                val ctx = context.get()
                ctx.enqueueWork {
                    (ctx.getBlockEntity(pos) as? SelfRefillingChestEntity)?.refillInterval =
                        refillIntervalSeconds.seconds
                }
                ctx.packetHandled = true
            }
        }
    }

    private var refillInventory: List<ItemStack> = generateSequence { ItemStack.EMPTY }.take(27).toList()
    var refillInterval: Duration? = null
        set(value) {
            field = value
            setChanged()
        }
    var lastRefillTime: Long? = null

    fun copyChestInventoryToRefill() {
        refillInventory = items.map(ItemStack::copy)
        if (lastRefillTime == null) lastRefillTime = level?.gameTime
        setChanged()
    }

    fun tick(level: Level, pos: BlockPos, state: BlockState) {
        val refillInterval = refillInterval ?: return
        val lastRefillTime = lastRefillTime
        if (lastRefillTime == null || lastRefillTime + refillInterval.inWholeTicks <= level.gameTime) {
            (0 until 27).forEach { items[it] = refillInventory[it].copy() }
            this.lastRefillTime = level.gameTime
            setChanged()
            level.sendBlockUpdated(pos, state, state, 3)
        }
    }

    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)
        tag.put(TAG_REFILL,
            ListTag().apply { refillInventory.asSequence().map { it.save(CompoundTag()) }.forEach(::add) })
        refillInterval?.let {
            tag.putLong(TAG_INTERVAL, it.inWholeSeconds)
        }
        lastRefillTime?.let {
            tag.putLong(TAG_LAST_REFILL, it)
        }
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        refillInventory = tag.getList(TAG_REFILL, Tag.TAG_COMPOUND.toInt()).map { ItemStack.of(it as CompoundTag) }
        if (tag.contains(TAG_INTERVAL)) refillInterval = tag.getLong(TAG_INTERVAL).seconds
        if (tag.contains(TAG_LAST_REFILL)) lastRefillTime = tag.getLong(TAG_LAST_REFILL)
    }

    override fun getUpdatePacket(): ClientboundBlockEntityDataPacket = ClientboundBlockEntityDataPacket.create(this)
    override fun getUpdateTag() = CompoundTag().also { saveAdditional(it) }
}

