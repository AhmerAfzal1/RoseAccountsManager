package com.ahmer.accounts.database.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ahmer.accounts.R

data class CategoryModel(
    @StringRes
    val category: Int,
    @DrawableRes
    val icon: Int
) {
    companion object {

        val advertising: CategoryModel = CategoryModel(
            category = R.string.category_advertising,
            icon = R.drawable.ic_exp_advertising,
        )

        val anniversary: CategoryModel = CategoryModel(
            category = R.string.category_anniversary,
            icon = R.drawable.ic_exp_anniversary,
        )

        val birthday: CategoryModel = CategoryModel(
            category = R.string.category_birthday,
            icon = R.drawable.ic_exp_birthday,
        )

        val charity: CategoryModel =
            CategoryModel(category = R.string.category_charity, icon = R.drawable.ic_exp_charity)
        val christmas: CategoryModel = CategoryModel(
            category = R.string.category_christmas,
            icon = R.drawable.ic_exp_christmas,
        )
        val cloths: CategoryModel =
            CategoryModel(category = R.string.category_cloths, icon = R.drawable.ic_exp_cloths)
        val concert: CategoryModel =
            CategoryModel(category = R.string.category_concert, icon = R.drawable.ic_exp_concert)
        val education: CategoryModel = CategoryModel(
            category = R.string.category_education,
            icon = R.drawable.ic_exp_education,
        )
        val electricity: CategoryModel = CategoryModel(
            category = R.string.category_electricity,
            icon = R.drawable.ic_exp_electricity,
        )
        val entertainment: CategoryModel = CategoryModel(
            category = R.string.category_entertainment,
            icon = R.drawable.ic_exp_entertainment,
        )
        val fees: CategoryModel =
            CategoryModel(category = R.string.category_fees, icon = R.drawable.ic_exp_fees)
        val food: CategoryModel =
            CategoryModel(category = R.string.category_food, icon = R.drawable.ic_exp_food)
        val furniture: CategoryModel = CategoryModel(
            category = R.string.category_furniture,
            icon = R.drawable.ic_exp_furniture,
        )
        val game: CategoryModel =
            CategoryModel(category = R.string.category_game, icon = R.drawable.ic_exp_game)
        val garbage: CategoryModel =
            CategoryModel(category = R.string.category_garbage, icon = R.drawable.ic_exp_garbage)
        val grocery: CategoryModel =
            CategoryModel(category = R.string.category_grocery, icon = R.drawable.ic_exp_grocery)
        val healthcare: CategoryModel = CategoryModel(
            category = R.string.category_healthcare,
            icon = R.drawable.ic_exp_healthcare,
        )
        val housing: CategoryModel =
            CategoryModel(category = R.string.category_housing, icon = R.drawable.ic_exp_housing)
        val insurance: CategoryModel = CategoryModel(
            category = R.string.category_insurance,
            icon = R.drawable.ic_exp_insurance,
        )
        val internet: CategoryModel =
            CategoryModel(category = R.string.category_internet, icon = R.drawable.ic_exp_internet)
        val investing: CategoryModel = CategoryModel(
            category = R.string.category_investing,
            icon = R.drawable.ic_exp_investing,
        )
        val loan: CategoryModel =
            CategoryModel(category = R.string.category_loan, icon = R.drawable.ic_exp_loan)
        val maintenance: CategoryModel = CategoryModel(
            category = R.string.category_maintenance,
            icon = R.drawable.ic_exp_maintenance,
        )
        val marketing: CategoryModel = CategoryModel(
            category = R.string.category_marketing,
            icon = R.drawable.ic_exp_marketing,
        )
        val marketing: CategoryModel = CategoryModel(
            category = R.string.category_marketing,
            icon = R.drawable.ic_exp_marketing,
        )

        val others: CategoryModel = CategoryModel(
            category = R.string.category_other,
            icon = R.drawable.ic_exp_other,
        )
    }
}