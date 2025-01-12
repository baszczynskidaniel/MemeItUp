package org.example.project.meme.presentation.menu

sealed interface MenuAction {
    data object OnSettings: MenuAction
    data object OnMultiPlayer: MenuAction
    data object OnLocalCoop: MenuAction
}