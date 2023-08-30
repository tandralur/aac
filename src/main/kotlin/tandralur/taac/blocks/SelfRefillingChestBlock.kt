package tandralur.taac.blocks

import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.ChestBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraftforge.network.NetworkHooks
import tandralur.taac.blocks.entity.ModBlockEntities
import tandralur.taac.blocks.entity.SelfRefillingChestEntity
import tandralur.taac.gui.SelfRefillingChestMenu

class SelfRefillingChestBlock(props: Properties) :
    ChestBlock(props.strength(-1f, 3600000f), { ModBlockEntities.SELF_REFILLING_CHEST.get() }) {

    @Deprecated("Deprecated in Java")
    override fun use(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {
        if (!player.isShiftKeyDown || level.isClientSide || !player.isCreative) {
            return super.use(state, level, pos, player, hand, hit)
        }

        val entity = level.getBlockEntity(pos)!! as SelfRefillingChestEntity
        NetworkHooks.openScreen(player as ServerPlayer, object : MenuProvider {
            override fun getDisplayName(): Component = this@SelfRefillingChestBlock.name
            override fun createMenu(syncId: Int, inv: Inventory, player: Player): AbstractContainerMenu =
                SelfRefillingChestMenu(syncId, entity)
        }) { buf -> buf.writeBlockPos(pos) }

        return InteractionResult.SUCCESS
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? =
        if (blockEntityType === ModBlockEntities.SELF_REFILLING_CHEST.get()) {
            val superTicker = super.getTicker(level, state, blockEntityType)
            BlockEntityTicker { level: Level, pos: BlockPos, state: BlockState, blockEntity: T ->
                (blockEntity as SelfRefillingChestEntity).tick(level, pos, state)
                superTicker?.tick(level, pos, state, blockEntity)
            }
        } else null

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = SelfRefillingChestEntity(pos, state)

    override fun appendHoverText(
        stack: ItemStack,
        level: BlockGetter?,
        tooltip: MutableList<Component>,
        flag: TooltipFlag
    ) {
        tooltip.add(
            Component.translatable("block.taac.self_refilling_chest.tooltip")
                .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
        )
        super.appendHoverText(stack, level, tooltip, flag)
    }
}