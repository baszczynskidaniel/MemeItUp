package org.example.project.core.presentation.design_system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import org.example.project.core.presentation.ui.LocalDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MIUCenterSurface(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable() (RowScope.() -> Unit) = {},
    expandedHeight: Dp = TopAppBarDefaults. TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults. windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(LocalDimensions.current.mediumPadding),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Column(
        modifier = modifier
    ) {
        MIUTopAppBar(
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            expandedHeight = expandedHeight,
            windowInsets = windowInsets,
            scrollBehavior = scrollBehavior,
        )
        MIUCenterSurface(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
        ) {
            content()
        }
    }
}

@Composable
fun MIUCenterSurface(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(LocalDimensions.current.mediumPadding),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable() (ColumnScope.() -> Unit),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(LocalDimensions.current.mediumPadding),
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxSurfaceWidth)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
        ) {
            content()
        }
    }
}