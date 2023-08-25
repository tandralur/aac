package tandralur.taac

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import tandralur.taac.blocks.ModBlocks
import tandralur.taac.items.ModItems
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist

@Mod(ArmoryAndCreatures.ID)
object ArmoryAndCreatures {
    const val ID = "taac"

    private val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        LOADING_CONTEXT.registerConfig(ModConfig.Type.SERVER, AacConfig.SERVER_CONFIG_SPEC)

        ModBlocks.REGISTRY.register(MOD_BUS)
        ModItems.REGISTRY.register(MOD_BUS)

        runForDist(
            clientTarget = {
                MOD_BUS.addListener(ArmoryAndCreatures::onClientSetup)
            },
            serverTarget = {
                MOD_BUS.addListener(ArmoryAndCreatures::onServerSetup)
            })
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
    }
}