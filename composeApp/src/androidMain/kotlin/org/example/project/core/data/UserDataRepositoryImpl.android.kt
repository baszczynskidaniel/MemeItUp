package org.example.project.core.data


import org.example.project.settings.domain.Language
import java.util.Locale

actual fun setAppLanguage(language: Language) {
    val locale = Locale(language.toString())
    Locale.setDefault(locale)
}