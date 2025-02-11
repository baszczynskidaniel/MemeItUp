package org.example.project.meme.presentation.menu

sealed interface MenuAction {

    data object OnJokeChange: MenuAction
}