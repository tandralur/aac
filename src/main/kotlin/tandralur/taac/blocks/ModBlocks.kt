package tandralur.taac.blocks

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import tandralur.taac.ArmoryAndCreatures

object ModBlocks {
    val REGISTRY: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, ArmoryAndCreatures.ID)

    val SELF_REFILLING_CHEST = REGISTRY.register("self_refilling_chest") {
        SelfRefillingChestBlock(BlockBehaviour.Properties.of(Material.WOOD))
    }
}