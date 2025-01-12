package org.example.project.settings.presentation.language

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.settings.domain.Language
import org.example.project.settings.presentation.components.LanguageIcon

@Composable
fun LanguageItem(
    language: Language,
    selected: Boolean,
    onClick: (Language) -> Unit,
    modifier: Modifier = Modifier,
    ) {
    ListItem(
        modifier = modifier.clickable {
            if(!selected) {
                onClick(language)
            }
        }.clip(RoundedCornerShape(LocalDimensions.current.mediumClip)),
        colors = ListItemDefaults.colors(
            containerColor = if(selected) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Transparent
        ),
        leadingContent = {
            LanguageIcon(language)
        },
        headlineContent = {
            Text(
                language.getOriginName(),
                fontWeight = if(selected) FontWeight.Bold else FontWeight.Normal,
               // color = if(selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        },
        trailingContent = {
            AnimatedVisibility(selected) {
                Icon(
                    AppIcons.DONE,
                    null,
                   // tint = if(selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}