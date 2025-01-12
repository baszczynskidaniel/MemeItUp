package org.example.project.settings.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import memeitup.composeapp.generated.resources.Res
import memeitup.composeapp.generated.resources.flag_great_britain
import memeitup.composeapp.generated.resources.flag_poland
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.settings.domain.Language
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LanguageIcon(
    language: Language,
    modifier: Modifier = Modifier,
) {
    val icon = when(language) {
        Language.POLISH -> vectorResource(Res.drawable.flag_poland)
        Language.ENGLISH -> vectorResource(Res.drawable.flag_great_britain)
    }
    Icon(modifier = modifier,
        imageVector = icon,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
