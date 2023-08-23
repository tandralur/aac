package tandralur.taac.items

import com.google.common.collect.ImmutableListMultimap
import com.google.common.collect.Multimap
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import net.minecraftforge.common.ToolAction
import net.minecraftforge.common.ToolActions

class BlockingSwordItem(private val damage: Float, attackSpeed: Float, props: Properties) : SwordItem(Tiers.IRON, 0, 0f, props) {
    private val scotlandModifiers: ImmutableListMultimap<Attribute, AttributeModifier> =
        ImmutableListMultimap.builder<Attribute, AttributeModifier>().run {
            put(
                Attributes.ATTACK_DAMAGE, AttributeModifier(
                    BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", damage - 1.0, AttributeModifier.Operation.ADDITION
                )
            )
            put(
                Attributes.ATTACK_SPEED, AttributeModifier(
                    BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed - 4.0, AttributeModifier.Operation.ADDITION
                )
            )
            build()
        }

    override fun getDamage() = damage

    // add shield block behavior

    override fun canPerformAction(stack: ItemStack, toolAction: ToolAction) =
        ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction)
    override fun getUseAnimation(stack: ItemStack) = UseAnim.BLOCK
    override fun getUseDuration(stack: ItemStack) = 72000

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val item = player.getItemInHand(hand)
        player.startUsingItem(hand)
        return InteractionResultHolder.consume(item)
    }

    override fun isValidRepairItem(stack: ItemStack, repairStack: ItemStack): Boolean = repairStack.`is`(Items.IRON_INGOT)

    override fun getAttributeModifiers(slot: EquipmentSlot, stack: ItemStack): Multimap<Attribute, AttributeModifier> =
        if (slot == EquipmentSlot.MAINHAND) scotlandModifiers else super.getAttributeModifiers(slot, stack)
}