package tandralur.taac

import net.minecraft.client.renderer.blockentity.ChestRenderer
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import tandralur.taac.blocks.ModBlocks
import tandralur.taac.blocks.entity.ModBlockEntities
import tandralur.taac.gui.ModMenus
import tandralur.taac.gui.ModScreens
import tandralur.taac.items.ModItems
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist

@Mod(ArmoryAndCreatures.ID)
object ArmoryAndCreatures {
    const val ID = "taac"

    init {
        LOADING_CONTEXT.registerConfig(ModConfig.Type.SERVER, AacConfig.SERVER_CONFIG_SPEC)

        ModBlocks.REGISTRY.register(MOD_BUS)
        ModItems.ITEMS.register(MOD_BUS)
        ModBlockEntities.BLOCK_ENTITIES.register(MOD_BUS)
        ModMenus.MENUS.register(MOD_BUS)
        ModNetwork.registerMessages()

        runForDist(
            clientTarget = {
                MOD_BUS.addListener(ArmoryAndCreatures::onClientSetup)
                MOD_BUS.addListener(ArmoryAndCreatures::registerEntityRenderers)
            },
            serverTarget = {
            })
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        event.enqueueWork(ModScreens::registerScreens)
    }

    private fun registerEntityRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerBlockEntityRenderer(ModBlockEntities.SELF_REFILLING_CHEST.get(), ::ChestRenderer)
    }
}