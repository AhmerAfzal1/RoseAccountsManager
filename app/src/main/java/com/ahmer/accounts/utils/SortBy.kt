package com.ahmer.accounts.utils

sealed class SortBy(val name: String) {
    data object Ascending : SortBy(name = "ascending")
    data object Descending : SortBy(name = "descending")

    companion object {
        private const val ASCENDING: String = "Ascending"
        private const val DESCENDING: String = "Descending"

        val listOfSortBy: List<Pair<SortBy, String>> by lazy {
            listOf(
                Ascending to ASCENDING,
                Descending to DESCENDING,
            )
        }

        fun getSortBy(sortOrder: SortBy): String {
            return when (sortOrder) {
                Ascending -> ASCENDING
                Descending -> DESCENDING
            }
        }

        fun valueOf(value: String): SortBy {
            return when (value) {
                Ascending.name -> Ascending
                Descending.name -> Descending
                else -> Ascending
            }
        }
    }
}