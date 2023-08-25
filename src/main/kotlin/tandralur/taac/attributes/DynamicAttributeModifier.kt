package tandralur.taac.attributes

import net.minecraft.world.entity.ai.attributes.AttributeModifier
import java.util.*

class DynamicAttributeModifier(
    id: UUID, name: String, operation: Operation, val amountFun: () -> Double
) : AttributeModifier(id, name, amountFun(), operation) {
    override fun getAmount() = amountFun()
}