package org.example.project.core.presentation.design_system

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.core.presentation.ui.LocalDimensions

@Composable
fun SmallSpacer() {
    Spacer(modifier = Modifier.height(LocalDimensions.current.smallPadding))
}

@Composable
fun MediumSpacer() {
    Spacer(modifier = Modifier.height(LocalDimensions.current.mediumPadding))
}

@Composable
fun BigSpacer() {
    Spacer(modifier = Modifier.height(LocalDimensions.current.bigPadding))
}