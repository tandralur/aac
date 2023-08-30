package tandralur.taac.blocks.entity

import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import tandralur.taac.ArmoryAndCreatures
import tandralur.taac.blocks.ModBlocks

object ModBlockEntities {
    val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArmoryAndCreatures.ID)

    val SELF_REFILLING_CHEST = BLOCK_ENTITIES.register("self_refilling_chest") {
        BlockEntityType.Builder.of(::SelfRefillingChestEntity, ModBlocks.SELF_REFILLING_CHEST.get()).build(null)
    }

}