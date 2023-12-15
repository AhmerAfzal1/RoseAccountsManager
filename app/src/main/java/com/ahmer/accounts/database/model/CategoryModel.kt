package com.ahmer.accounts.database.model

import androidx.annotation.DrawableRes
import com.ahmer.accounts.R

data class CategoryModel(
    var title: String,
    @DrawableRes
    val icon: Int
) {
    companion object {
        val advertising: CategoryModel = CategoryModel(
            title = "Advertising",
            icon = R.drawable.ic_exp_advertising,
        )
        val anniversary: CategoryModel = CategoryModel(
            title = "Anniversary",
            icon = R.drawable.ic_exp_anniversary,
        )
        val birthday: CategoryModel = CategoryModel(
            title = "Birthday",
            icon = R.drawable.ic_exp_birthday,
        )
        val charity: CategoryModel = CategoryModel(
            title = "Charity",
            icon = R.drawable.ic_exp_charity,
        )
        val christmas: CategoryModel = CategoryModel(
            title = "Christmas",
            icon = R.drawable.ic_exp_christmas,
        )
        val cloths: CategoryModel = CategoryModel(
            title = "Cloths",
            icon = R.drawable.ic_exp_cloths,
        )
        val concert: CategoryModel = CategoryModel(
            title = "Concert",
            icon = R.drawable.ic_exp_concert,
        )
        val education: CategoryModel = CategoryModel(
            title = "Education",
            icon = R.drawable.ic_exp_education,
        )
        val electricity: CategoryModel = CategoryModel(
            title = "Electricity",
            icon = R.drawable.ic_exp_electricity,
        )
        val entertainment: CategoryModel = CategoryModel(
            title = "Entertainment",
            icon = R.drawable.ic_exp_entertainment,
        )
        val fees: CategoryModel = CategoryModel(
            title = "Fees",
            icon = R.drawable.ic_exp_fees,
        )
        val food: CategoryModel = CategoryModel(
            title = "Food",
            icon = R.drawable.ic_exp_food,
        )
        val furniture: CategoryModel = CategoryModel(
            title = "Furniture",
            icon = R.drawable.ic_exp_furniture,
        )
        val game: CategoryModel = CategoryModel(
            title = "Game",
            icon = R.drawable.ic_exp_game,
        )
        val garbage: CategoryModel = CategoryModel(
            title = "Garbage",
            icon = R.drawable.ic_exp_garbage,
        )
        val grocery: CategoryModel = CategoryModel(
            title = "Grocery",
            icon = R.drawable.ic_exp_grocery,
        )
        val healthcare: CategoryModel = CategoryModel(
            title = "Healthcare",
            icon = R.drawable.ic_exp_healthcare,
        )
        val housing: CategoryModel = CategoryModel(
            title = "Housing",
            icon = R.drawable.ic_exp_housing,
        )
        val insurance: CategoryModel = CategoryModel(
            title = "Insurance",
            icon = R.drawable.ic_exp_insurance,
        )
        val internet: CategoryModel = CategoryModel(
            title = "Internet",
            icon = R.drawable.ic_exp_internet,
        )
        val investing: CategoryModel = CategoryModel(
            title = "Investing",
            icon = R.drawable.ic_exp_investing,
        )
        val loan: CategoryModel = CategoryModel(
            title = "Loan",
            icon = R.drawable.ic_exp_loan,
        )
        val maintenance: CategoryModel = CategoryModel(
            title = "Maintenance",
            icon = R.drawable.ic_exp_maintenance,
        )
        val marketing: CategoryModel = CategoryModel(
            title = "Marketing",
            icon = R.drawable.ic_exp_marketing,
        )
        val movies: CategoryModel = CategoryModel(
            title = "Movies",
            icon = R.drawable.ic_exp_movies,
        )
        val office: CategoryModel = CategoryModel(
            title = "Office",
            icon = R.drawable.ic_exp_office,
        )
        val others: CategoryModel = CategoryModel(
            title = "Other",
            icon = R.drawable.ic_exp_other,
        )
        val phones: CategoryModel = CategoryModel(
            title = "Phone",
            icon = R.drawable.ic_exp_phone,
        )
        val rent: CategoryModel = CategoryModel(
            title = "Rent",
            icon = R.drawable.ic_exp_rent,
        )
        val restaurant: CategoryModel = CategoryModel(
            title = "Restaurant",
            icon = R.drawable.ic_exp_restaurant,
        )
        val salary: CategoryModel = CategoryModel(
            title = "Salary",
            icon = R.drawable.ic_exp_salary,
        )
        val saving: CategoryModel = CategoryModel(
            title = "Saving",
            icon = R.drawable.ic_exp_saving,
        )
        val shoes: CategoryModel = CategoryModel(
            title = "Shoes",
            icon = R.drawable.ic_exp_shoes,
        )
        val shopping: CategoryModel = CategoryModel(
            title = "Shopping",
            icon = R.drawable.ic_exp_shopping,
        )
        val subscriptions: CategoryModel = CategoryModel(
            title = "Subscriptions",
            icon = R.drawable.ic_exp_subscriptions,
        )
        val tax: CategoryModel = CategoryModel(
            title = "Tax",
            icon = R.drawable.ic_exp_tax,
        )
        val transportation: CategoryModel = CategoryModel(
            title = "Transportation",
            icon = R.drawable.ic_exp_transportation,
        )
        val travel: CategoryModel = CategoryModel(
            title = "Travel",
            icon = R.drawable.ic_exp_travel,
        )
        val utilities: CategoryModel = CategoryModel(
            title = "Utilities",
            icon = R.drawable.ic_exp_utilities,
        )
        val water: CategoryModel = CategoryModel(
            title = "Water",
            icon = R.drawable.ic_exp_water,
        )
        val wedding: CategoryModel = CategoryModel(
            title = "Wedding",
            icon = R.drawable.ic_exp_wedding,
        )

        val listCategories: List<CategoryModel> by lazy {
            listOf(
                advertising, anniversary, birthday, charity, christmas, cloths, concert, education,
                electricity, entertainment, fees, food, furniture, game, garbage, grocery,
                healthcare, housing, insurance, internet, investing, loan, maintenance, marketing,
                movies, office, others, phones, rent, restaurant, salary, saving, shoes, shopping,
                subscriptions, tax, transportation, travel, utilities, water, wedding,
            )
        }

        fun getIconByTitle(title: String): Int {
            return listCategories.find { it.title == title }?.icon ?: others.icon
        }
    }
}