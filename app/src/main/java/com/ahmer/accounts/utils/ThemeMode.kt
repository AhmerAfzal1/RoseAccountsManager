package com.ahmer.accounts.utils

sealed class ThemeMode(val name: String) {
    data object Dark : ThemeMode(name = "dark")
    data object Light : ThemeMode(name = "light")
    data object System : ThemeMode(name = "system")

    companion object {
        private const val TITLE_DARK: String = "Dark"
        private const val TITLE_LIGHT: String = "Light"
        private const val TITLE_SYSTEM: String = "System default"

        /**
         * Lazily initialized list of theme modes paired with their display titles.
         */
        val themeModes: List<Pair<ThemeMode, String>> by lazy {
            listOf(
                Dark to TITLE_DARK,
                Light to TITLE_LIGHT,
                System to TITLE_SYSTEM
            )
        }

        /**
         * Returns the display title for the given [themeMode].
         *
         * @param themeMode the theme mode.
         * @return the corresponding display title.
         */
        fun getDisplayTitle(themeMode: ThemeMode): String = when (themeMode) {
            Dark -> TITLE_DARK
            Light -> TITLE_LIGHT
            System -> TITLE_SYSTEM

        }

        /**
         * Maps the provided string [value] to the corresponding [ThemeMode].
         * Defaults to [System] if the value does not match any mode.
         *
         * @param value the string representation of the theme mode.
         * @return the matching [ThemeMode] instance, or [System] if no match is found.
         */
        fun valueOf(value: String): ThemeMode = when (value) {
            Dark.name -> Dark
            Light.name -> Light
            System.name -> System
            else -> System
        }

    }
}
