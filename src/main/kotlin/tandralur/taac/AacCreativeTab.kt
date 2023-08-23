package tandralur.taac

import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object AacCreativeTab : CreativeModeTab(TABS.size, "taac") {
    override fun makeIcon() = ItemStack(Items.BLACK_CANDLE)
}