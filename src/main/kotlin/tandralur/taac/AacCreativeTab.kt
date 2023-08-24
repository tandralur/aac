package tandralur.taac

import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import tandralur.taac.items.ModItems

object AacCreativeTab : CreativeModeTab(TABS.size, "taac") {
    override fun makeIcon() = ItemStack(ModItems.SCOTLAND_FOREVER)
}