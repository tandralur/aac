package tandralur.taac

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val Long.ticks: Duration get() = (this * 50).milliseconds
val Duration.inWholeTicks get() = inWholeMilliseconds / 50