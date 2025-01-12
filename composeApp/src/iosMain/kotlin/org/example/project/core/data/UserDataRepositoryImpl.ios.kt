package org.example.project.core.data

import org.example.project.settings.domain.Language

actual fun setAppLanguage(language: Language) {
    NSUserDefaults.standardUserDefaults.setObject(arrayListOf(language),”AppleLanguages”)
}