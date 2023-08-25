package tandralur.taac.items

import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import tandralur.taac.AacCreativeTab
import tandralur.taac.ArmoryAndCreatures
import thedarkcolour.kotlinforforge.forge.registerObject

object ModItems {
    val REGISTRY: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, ArmoryAndCreatures.ID)

    val SCOTLAND_FOREVER by REGISTRY.registerObject("scotland_forever") {
        ScotlandForeverItem(Item.Properties().tab(AacCreativeTab))
    }
}