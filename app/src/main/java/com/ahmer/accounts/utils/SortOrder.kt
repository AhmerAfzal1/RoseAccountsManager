package com.ahmer.accounts.utils

sealed class SortOrder(val sortBy: SortBy) {
    class Amount(sortBy: SortBy) : SortOrder(sortBy = sortBy)
    class Date(sortBy: SortBy) : SortOrder(sortBy = sortBy)
    class Name(sortBy: SortBy) : SortOrder(sortBy = sortBy)

    fun copy(sortBy: SortBy): SortOrder {
        return when (this) {
            is Amount -> Amount(sortBy = sortBy)
            is Date -> Date(sortBy = sortBy)
            is Name -> Name(sortBy = sortBy)
        }
    }
}