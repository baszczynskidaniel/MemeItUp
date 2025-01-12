package org.example.project.settings.presentation.settings_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import memeitup.composeapp.generated.resources.Res
import memeitup.composeapp.generated.resources.dark_theme
import memeitup.composeapp.generated.resources.dynamic_color
import memeitup.composeapp.generated.resources.dynamic_color_description
import memeitup.composeapp.generated.resources.language
import memeitup.composeapp.generated.resources.navigate_back
import memeitup.composeapp.generated.resources.settings
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.isAndroidWithSDK
import org.example.project.settings.presentation.components.LanguageIcon
import org.example.project.settings.presentation.language.LanguageItem
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLanguage: () -> Unit,
    onAction: (SettingsAction) -> Unit,
    state: SettingsState,
    modifier: Modifier = Modifier)
{
    MIUCenterSurface(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBack,
            ) {
                Icon(
                    AppIcons.BACK,
                    stringResource(Res.string.navigate_back)
                )
            }
        },
        title = {
            Text(stringResource(Res.string.settings))
        },
    ) {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            headlineContent = { Text(stringResource(Res.string.dark_theme)) },
            trailingContent = {
                Switch(
                    checked = state.settings.darkTheme,
                    onCheckedChange = {
                        onAction(SettingsAction.OnDarkThemeChange(!state.settings.darkTheme))
                    }
                )
            }
        )
        if(isAndroidWithSDK(31)) {
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                headlineContent = { Text(stringResource(Res.string.dynamic_color)) },
                supportingContent = {
                    Text(stringResource(Res.string.dynamic_color_description))
                },
                trailingContent = {
                    Switch(
                        checked = state.settings.dynamicColor,
                        onCheckedChange = {
                            onAction(SettingsAction.OnDynamicColorChange(!state.settings.dynamicColor))
                        }
                    )
                }
            )
        }
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLanguage() },
            headlineContent = { Text(stringResource(Res.string.language)) },
            trailingContent = {
                LanguageIcon(state.settings.language)
            }
        )

    }
}

