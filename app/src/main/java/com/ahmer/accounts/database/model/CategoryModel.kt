package com.ahmer.accounts.database.model

import androidx.annotation.DrawableRes

data class CategoryModel(
    val category: String,
    @DrawableRes
    val icon: Int
)