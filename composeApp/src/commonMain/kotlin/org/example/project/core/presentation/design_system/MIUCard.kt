package org.example.project.core.presentation.design_system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.core.presentation.ui.LocalDimensions

@Composable
fun MIUCard(
    modifier: Modifier = Modifier,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(LocalDimensions.current.mediumPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.mediumPadding)
        ) {
            content()
        }
    }
}