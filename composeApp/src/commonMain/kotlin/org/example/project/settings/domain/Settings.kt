package org.example.project.settings.domain


data class Settings(
     val darkTheme: Boolean = true,
     val dynamicColor: Boolean = false,
     val language: Language = Language.ENGLISH,
)

enum class AppTheme {
    DARK,
    LIGHT,
    SYSTEM;

    override fun toString(): String {
        return when(this) {
            DARK -> "dark"
            LIGHT -> "light"
            SYSTEM -> "system"
        }
    }

    companion object {
        fun fromString(value: String): AppTheme? {
            return when (value.lowercase()) {
                "dark" -> DARK
                "light" -> LIGHT
                "system" -> SYSTEM
                else -> null
            }
        }
    }
}

enum class Language(val isoFormat: String) {
    POLISH("pl"),
    ENGLISH("en");

    override fun toString(): String {
        return when(this) {
            POLISH -> "pl"
            ENGLISH -> "en"
        }
    }

    fun getOriginName(): String {
        return when(this) {
            POLISH -> "Polski"
            ENGLISH -> "English"
        }
    }

    companion object {
        fun fromString(isoFormat: String): Language? {
            return when (isoFormat) {
                "pl" -> POLISH
                "en" -> ENGLISH
                else -> null
            }
        }
    }
}



