package tandralur.taac

import net.minecraftforge.common.ForgeConfigSpec
import kotlin.reflect.KProperty

operator fun <T, O> ForgeConfigSpec.ConfigValue<T>.getValue(owner: O, property: KProperty<*>): T =
    try {
        get()
    } catch (ex: IllegalStateException) {
        default
    }

operator fun <T, O> ForgeConfigSpec.ConfigValue<T>.setValue(owner: O, property: KProperty<*>, value: T) = set(value)