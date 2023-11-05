package com.ahmer.accounts.utils

sealed class ThemeMode(val name: String) {
    data object Dark : ThemeMode(name = "dark")
    data object Light : ThemeMode(name = "light")
    data object System : ThemeMode(name = "system")

    companion object {
        private const val DARK: String = "Dark"
        private const val LIGHT: String = "Light"
        private const val SYSTEM: String = "System default"

        val listOfThemeModes: List<Pair<ThemeMode, String>> by lazy {
            listOf(
                Dark to DARK,
                Light to LIGHT,
                System to SYSTEM
            )
        }

        fun getThemeModesTitle(themeMode: ThemeMode): String {
            return when (themeMode) {
                Dark -> DARK
                Light -> LIGHT
                System -> SYSTEM
            }
        }

        fun valueOf(value: String): ThemeMode {
            return when (value) {
                Dark.name -> Dark
                Light.name -> Light
                System.name -> System
                else -> System
            }
        }
    }
}
