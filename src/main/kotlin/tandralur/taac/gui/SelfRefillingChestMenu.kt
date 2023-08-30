package tandralur.taac.gui

import net.minecraft.core.GlobalPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import tandralur.taac.ModNetwork
import tandralur.taac.blocks.entity.SelfRefillingChestEntity
import tandralur.taac.blocks.entity.SelfRefillingChestEntity.Companion.CopyInventoryMessage
import tandralur.taac.blocks.entity.SelfRefillingChestEntity.Companion.SetRefillIntervalMessage

class SelfRefillingChestMenu(windowId: Int, val entity: SelfRefillingChestEntity) :
    AbstractContainerMenu(ModMenus.SELF_REFILLING_CHEST, windowId) {

    companion object {
        fun fromNetwork(windowId: Int, inv: Inventory, buf: FriendlyByteBuf): SelfRefillingChestMenu {
            val blockEntity = inv.player.level.getBlockEntity(buf.readBlockPos())
            return SelfRefillingChestMenu(windowId, blockEntity as SelfRefillingChestEntity)
        }
    }

    override fun quickMoveStack(pPlayer: Player, pIndex: Int): ItemStack = ItemStack.EMPTY
    override fun stillValid(pPlayer: Player) = true

    fun setRefillInterval(timeSeconds: Long?) {
        if (timeSeconds == null) return

        ModNetwork.NETWORK_CHANNEL.sendToServer(
            SetRefillIntervalMessage(
                GlobalPos.of(entity.level?.dimension()!!, entity.blockPos),
                timeSeconds,
            )
        )
    }

    fun copyInventory() {
        ModNetwork.NETWORK_CHANNEL.sendToServer(
            CopyInventoryMessage(
                GlobalPos.of(entity.level?.dimension()!!, entity.blockPos)
            )
        )
    }
}