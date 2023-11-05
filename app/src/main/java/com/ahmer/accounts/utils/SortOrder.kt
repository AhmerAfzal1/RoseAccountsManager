package com.ahmer.accounts.utils

sealed class SortOrder(val name: String) {
    data object Amount : SortOrder(name = "amount")
    data object Date : SortOrder(name = "date")
    data object Name : SortOrder(name = "name")

    companion object {
        private const val AMOUNT: String = "Amount"
        private const val DATE: String = "Recent Added"
        private const val NAME: String = "Name"

        val listOfSortOrder: List<Pair<SortOrder, String>> by lazy {
            listOf(
                Date to DATE,
                Name to NAME,
                Amount to AMOUNT
            )
        }

        fun getSortOrder(sortOrder: SortOrder): String {
            return when (sortOrder) {
                Amount -> AMOUNT
                Date -> DATE
                Name -> NAME
            }
        }

        fun valueOf(value: String): SortOrder {
            return when (value) {
                Amount.name -> Amount
                Date.name -> Date
                Name.name -> Name
                else -> Date
            }
        }
    }
}