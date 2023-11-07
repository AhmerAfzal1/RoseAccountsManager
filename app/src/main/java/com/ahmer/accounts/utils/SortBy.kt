package com.ahmer.accounts.utils

sealed class SortBy(val name: String) {
    data object Ascending : SortBy(name = "ascending")
    data object Descending : SortBy(name = "descending")

    companion object {
        fun valueOf(value: String): SortBy {
            return when (value) {
                Ascending.name -> Ascending
                Descending.name -> Descending
                else -> Ascending
            }
        }
    }
}