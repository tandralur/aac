package tandralur.taac.items

import com.google.common.collect.ImmutableListMultimap
import com.google.common.collect.Multimap
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tiers
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraftforge.common.ToolAction
import net.minecraftforge.common.ToolActions
import tandralur.taac.AacConfig
import tandralur.taac.attributes.DynamicAttributeModifier
import tandralur.taac.getValue

class ScotlandForeverItem(props: Properties) : SwordItem(Tiers.IRON, 0, 0f, props) {
    private val config = AacConfig.SERVER_CONFIG.items.scotlandForever
    private val attackDamage by config.damage
    private val attackSpeed by config.attackSpeed

    private val blockSwordModifiers = ImmutableListMultimap.builder<Attribute, AttributeModifier>().apply {
        put(Attributes.ATTACK_DAMAGE, DynamicAttributeModifier(
            BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", Operation.ADDITION
        ) { attackDamage - 1.0 })
        put(Attributes.ATTACK_SPEED, DynamicAttributeModifier(
            BASE_ATTACK_SPEED_UUID, "Weapon modifier", Operation.ADDITION
        ) { attackSpeed - 4.0 })
    }.build()

    override fun getDamage() = attackDamage.toFloat()

    override fun canPerformAction(stack: ItemStack, toolAction: ToolAction): Boolean =
        ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction)

    override fun isValidRepairItem(stack: ItemStack, repairStack: ItemStack) = false

    override fun getAttributeModifiers(slot: EquipmentSlot, stack: ItemStack): Multimap<Attribute, AttributeModifier> =
        if (slot == EquipmentSlot.MAINHAND) blockSwordModifiers else super.getAttributeModifiers(slot, stack)

    override fun getEnchantmentValue(stack: ItemStack?) = 22

    override fun getName(pStack: ItemStack): Component =
        Component.translatable(pStack.descriptionId).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC)

    override fun canAttackBlock(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player) =
        !pPlayer.isCreative

    override fun getDestroySpeed(pStack: ItemStack, pState: BlockState): Float = if (pState.`is`(Blocks.COBWEB)) {
        15.0f
    } else {
        val material = pState.material
        if (material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !pState.`is`(BlockTags.LEAVES) && material != Material.VEGETABLE) 1.0f else 1.5f
    }

    override fun isCorrectToolForDrops(pBlock: BlockState) = pBlock.`is`(Blocks.COBWEB)

    override fun getMaxDamage(stack: ItemStack?) = 0

    override fun canBeDepleted() = false

    override fun canDisableShield(stack: ItemStack, shield: ItemStack, entity: LivingEntity, attacker: LivingEntity) =
        true
}