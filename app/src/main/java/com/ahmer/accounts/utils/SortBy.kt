package com.ahmer.accounts.utils

sealed class SortBy(val name: String) {
    data object Ascending : SortBy(name = "ascending")
    data object Descending : SortBy(name = "descending")

    companion object {
        /**
         * Returns the matching [SortBy] instance for the provided [value].
         *
         * @param value the string representation of the sort order.
         * @return the corresponding [SortBy] if matched; otherwise, it defaults to [Ascending].
         */
        fun valueOf(value: String): SortBy {
            return when (value) {
                Ascending.name -> Ascending
                Descending.name -> Descending
                else -> Ascending
            }
        }
    }
}