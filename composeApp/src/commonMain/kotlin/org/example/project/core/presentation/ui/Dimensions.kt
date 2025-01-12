package org.example.project.core.presentation.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimensions(
    val mediumPadding: Dp = 12.dp,
    val smallPadding: Dp = mediumPadding / 2,
    val bigPadding: Dp = mediumPadding * 2,
    val logo: Dp = 196.dp,

    val minFieldSize: Dp = 24.dp,

    val dialogMaxWidth: Dp = 400.dp,
    val highlightWidth: Dp = 2.dp,
    val bigClip: Dp = 32.dp,
    val mediumClip: Dp = 16.dp,
    val dividerThickness: Dp = 0.5.dp,
    val alertDialogWidth: Dp = 400.dp,
    val bigIconButton: Dp = 72.dp,
    val bigIcon: Dp = 36.dp,
    val maxSurfaceWidth: Dp = 700.dp,
    val maxButtonWidth: Dp = 480.dp
)

val LocalDimensions = staticCompositionLocalOf { Dimensions() }
