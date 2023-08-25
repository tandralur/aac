package tandralur.taac

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.Builder
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue

object AacConfig {
    val SERVER_CONFIG: ServerConfig
    val SERVER_CONFIG_SPEC: ForgeConfigSpec

    init {
        val (serverConfig, spec) = Builder().configure { ServerConfig(it) }
        SERVER_CONFIG = serverConfig
        SERVER_CONFIG_SPEC = spec
    }

    class ServerConfig(builder: Builder) {
        val items = builder.within("items") { ItemConfig(it) }
    }

    class ItemConfig(builder: Builder) {
        val scotlandForever = builder.comment("The claymore Scotland Forever")
            .within("scotlandForever") { WeaponConfig(it, defaultDamage = 12.0, defaultAttackSpeed = 0.9) }
    }

    class WeaponConfig(builder: Builder, defaultDamage: Double = 6.0, defaultAttackSpeed: Double = 1.6) {
        val damage =
            builder.addInRange("damage", defaultDamage, 0.0, 1e9, "Raw damage, i.e. half harts of damage per swing")
        val attackSpeed = builder.addInRange("attackSpeed", defaultAttackSpeed, 0.0, 1e9, "Attacks per second")
    }
}

fun <T> Builder.within(path: String, exec: Builder.(Builder) -> T): T {
    push(path)
    val res = exec(this)
    pop()
    return res
}

fun Builder.addInRange(path: String, default: Double, min: Double, max: Double, comment: String = ""): DoubleValue {
    val defaultComment = "Default: $default"
    if (comment.isNotEmpty()) comment(comment)
    comment(defaultComment)
    return defineInRange(path, default, min, max)
}