package tandralur.taac.items

import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import tandralur.taac.AacCreativeTab
import tandralur.taac.ArmoryAndCreatures
import tandralur.taac.blocks.ModBlocks
import thedarkcolour.kotlinforforge.forge.registerObject

object ModItems {
    val ITEMS: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, ArmoryAndCreatures.ID)


    private fun getProps() = Item.Properties().tab(AacCreativeTab)
    private fun <T : Item> register(name: String, supplier: () -> T) = ITEMS.registerObject(name, supplier)

    val SCOTLAND_FOREVER by register("scotland_forever") { ScotlandForeverItem(getProps()) }

    val SELF_REFILLING_CHEST: RegistryObject<SelfRefillingChestItem> =
        ModBlocks.SELF_REFILLING_CHEST.let {
            ITEMS.register(it.id.path) {
                SelfRefillingChestItem(
                    it.get(), getProps()
                )
            }
        }
}

