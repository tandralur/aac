package tandralur.taac.items

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.ItemTransforms
import net.minecraft.core.BlockPos
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.extensions.common.IClientItemExtensions
import tandralur.taac.blocks.ModBlocks
import tandralur.taac.blocks.entity.SelfRefillingChestEntity
import java.util.function.Consumer


class SelfRefillingChestItem(block: Block, props: Properties) : BlockItem(block, props) {
    override fun initializeClient(consumer: Consumer<IClientItemExtensions>) {
        consumer.accept(object : IClientItemExtensions {
            override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
                return object : BlockEntityWithoutLevelRenderer(
                    Minecraft.getInstance().blockEntityRenderDispatcher,
                    Minecraft.getInstance().entityModels,
                ) {
                    val entity = SelfRefillingChestEntity(
                        BlockPos.ZERO, ModBlocks.SELF_REFILLING_CHEST.get().defaultBlockState()
                    )

                    override fun renderByItem(
                        stack: ItemStack,
                        transformType: ItemTransforms.TransformType,
                        poseStack: PoseStack,
                        buffer: MultiBufferSource,
                        packedLight: Int,
                        packedOverlay: Int
                    ) {
                        if (stack.item === block.asItem()) {
                            Minecraft.getInstance().blockEntityRenderDispatcher.renderItem(
                                entity, poseStack, buffer, packedLight, packedOverlay
                            )
                        }
                    }
                }
            }
        })
    }
}