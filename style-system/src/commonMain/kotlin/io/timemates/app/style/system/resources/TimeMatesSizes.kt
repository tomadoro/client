package io.timemates.app.style.system.resources

import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class TimeMatesSizes(
    val xxxxs: Dp = 2.dp,
    val xxxs: Dp = 4.dp,
    val xxs: Dp = 8.dp,
    val xs: Dp = 12.dp,
    val s: Dp = 16.dp,
    val m: Dp = 20.dp,
    val l: Dp = 24.dp,
)

@Stable
fun timeMatesSizes(): TimeMatesSizes = TimeMatesSizes()

val LocalSizes: ProvidableCompositionLocal<TimeMatesSizes> =
    staticCompositionLocalOf { TimeMatesSizes() }