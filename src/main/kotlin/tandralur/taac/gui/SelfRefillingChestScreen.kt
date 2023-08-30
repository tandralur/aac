package tandralur.taac.gui

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class SelfRefillingChestScreen(menu: SelfRefillingChestMenu, playerInv: Inventory, title: Component) :
    AbstractContainerScreen<SelfRefillingChestMenu>(menu, playerInv, title) {

    companion object {
        val backgroundTexture = ResourceLocation("taac:textures/gui/generic_blank.png")
    }

    override fun init() {
        super.init()
        val left = (width - imageWidth) / 2
        val top = (height - imageHeight) / 2
        addRenderableWidget(EditBox(
            font,
            left + 5,
            top + 20,
            166,
            16,
            Component.literal("Refill time interval in seconds")
        ).apply {
            setBordered(true)
            setEditable(true)
            setMaxLength(256)
            setFilter {
                it.toIntOrNull() != null || it.isEmpty()
            }
            setResponder {
                if (it.isEmpty()) setSuggestion("0")
                else setSuggestion("")
                menu.setRefillInterval(if (it.isEmpty()) null else it.toLong())
            }
            setSuggestion("0")
            value = menu.entity.refillInterval?.inWholeSeconds?.toString() ?: ""
        })
        addRenderableWidget(Button(left + 4, top + 40, 168, 20, Component.literal("Set refill from chest")) {
            menu.copyInventory()
        })
        if (menu.entity.lastRefillTime == null) {
            menu.copyInventory()
        }
    }

    override fun renderBg(poseStack: PoseStack, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        renderBackground(poseStack)

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        val left = (width - imageWidth) / 2
        val top = (height - imageHeight) / 2
        RenderSystem.setShaderTexture(0, backgroundTexture)
        GuiComponent.blit(poseStack, left, top, 0f, 0f, imageWidth, imageHeight, imageWidth, imageHeight)
    }

    override fun renderLabels(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int) {
        font.draw(pPoseStack, Component.literal("Refill interval in seconds"), 4f, 8f, 0x404040)
    }
}