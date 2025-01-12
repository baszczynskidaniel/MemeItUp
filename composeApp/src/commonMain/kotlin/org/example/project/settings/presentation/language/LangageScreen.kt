package org.example.project.settings.presentation.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import memeitup.composeapp.generated.resources.Res
import memeitup.composeapp.generated.resources.language
import memeitup.composeapp.generated.resources.navigate_back
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.settings.domain.Language
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    onBack: () -> Unit,
    onAction: (LanguageAction) -> Unit,
    state: LanguageState,
    modifier: Modifier = Modifier
) {
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
            Text(stringResource(Res.string.language))
        },
    ) {
        Language.entries.forEach { language ->
            LanguageItem(
                language = language,
                selected = language == state.selectedLanguage,
                onClick = { onAction(LanguageAction.OnLanguageChange(language)) }
            )
        }
    }
}


