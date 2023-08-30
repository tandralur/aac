package tandralur.taac.gui

import net.minecraft.client.gui.screens.MenuScreens

object ModScreens {
    fun registerScreens() {
        MenuScreens.register(ModMenus.SELF_REFILLING_CHEST, ::SelfRefillingChestScreen)
    }
}