package org.example.project.meme.presentation.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import memeitup.composeapp.generated.resources.Res
import memeitup.composeapp.generated.resources.language
import memeitup.composeapp.generated.resources.local
import memeitup.composeapp.generated.resources.multiplayer
import memeitup.composeapp.generated.resources.navigate_back
import memeitup.composeapp.generated.resources.settings
import org.example.project.core.presentation.design_system.BigSpacer
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onSettings: () -> Unit,
    onLocal: () -> Unit,
    onMultiplayer: () -> Unit,
    modifier: Modifier = Modifier,
    ) {

    MIUCenterSurface(
        modifier = modifier,
        actions = {
            IconButton(
                onClick = onSettings,
            ) {
                Icon(
                    AppIcons.SETTINGS,
                    stringResource(Res.string.settings)
                )
            }
        },
        title = {},
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
            onClick = onMultiplayer
        ) {
            Text(
                stringResource(Res.string.multiplayer)
            )
        }
        BigSpacer()
        OutlinedButton(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
            onClick = onLocal
        ) {
            Text(
                stringResource(Res.string.local)
            )
        }
    }
}