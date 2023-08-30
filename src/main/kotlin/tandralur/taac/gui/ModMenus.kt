package tandralur.taac.gui

import net.minecraft.world.inventory.MenuType
import net.minecraftforge.common.extensions.IForgeMenuType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import tandralur.taac.ArmoryAndCreatures
import thedarkcolour.kotlinforforge.forge.registerObject

object ModMenus {
    val MENUS: DeferredRegister<MenuType<*>> =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, ArmoryAndCreatures.ID)
    val SELF_REFILLING_CHEST: MenuType<SelfRefillingChestMenu> by MENUS.registerObject("self_refilling_chest") {
        IForgeMenuType.create(SelfRefillingChestMenu::fromNetwork)
    }
}