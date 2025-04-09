package com.ahmer.accounts.utils

sealed class SortOrder(val sortBy: SortBy) {
    class Amount(sortBy: SortBy) : SortOrder(sortBy = sortBy)
    class Date(sortBy: SortBy) : SortOrder(sortBy = sortBy)
    class Name(sortBy: SortBy) : SortOrder(sortBy = sortBy)

    /**
     * Creates a copy of the current instance with an updated [sortBy] value while
     * preserving the subclass type.
     *
     * @param sortBy the new sorting direction.
     * @return a new instance of the same [SortOrder] subtype with the updated [sortBy] value.
     */
    fun copy(sortBy: SortBy): SortOrder = when (this) {
        is Amount -> Amount(sortBy = sortBy)
        is Date -> Date(sortBy = sortBy)
        is Name -> Name(sortBy = sortBy)
    }

}