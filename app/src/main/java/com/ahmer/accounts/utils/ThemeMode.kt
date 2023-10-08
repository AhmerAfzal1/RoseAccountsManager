package com.ahmer.accounts.utils

sealed class ThemeMode(val name: String) {
    data object Dark : ThemeMode(name = "dark")
    data object Light : ThemeMode(name = "light")
    data object System : ThemeMode(name = "system")

    companion object {
        val listOfThemeModes: List<Pair<ThemeMode, String>> by lazy {
            listOf(
                Dark to "Dark",
                Light to "Light",
                System to "System default"
            )
        }

        fun getThemeModesTitle(themeMode: ThemeMode): String {
            return when (themeMode) {
                Dark -> "Dark"
                Light -> "Light"
                System -> "System default"
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
