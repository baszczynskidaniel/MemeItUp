package org.example.project.settings.presentation.language

import org.example.project.settings.domain.Language

sealed interface LanguageAction {
    data class OnLanguageChange(val languageChange: Language): LanguageAction
}