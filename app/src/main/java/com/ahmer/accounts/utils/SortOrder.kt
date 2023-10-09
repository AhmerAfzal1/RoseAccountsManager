package com.ahmer.accounts.utils

sealed class SortOrder(val name: String) {
    data object Date : SortOrder(name = "date")
    data object Name : SortOrder(name = "name")

    companion object {
        fun valueOf(value: String): SortOrder {
            return when (value) {
                Date.name -> Date
                Name.name -> Name
                else -> Date
            }
        }
    }
}