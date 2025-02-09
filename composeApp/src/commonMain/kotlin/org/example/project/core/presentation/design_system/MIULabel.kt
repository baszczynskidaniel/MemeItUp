package org.example.project.core.presentation.design_system

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MIULabel(
    text: String,
    modifier: Modifier = Modifier,

) {
    Text(
        text,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary
    )
}

