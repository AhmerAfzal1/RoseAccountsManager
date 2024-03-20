package com.ahmer.accounts.database.model

import androidx.annotation.DrawableRes
import com.ahmer.accounts.R
import com.ahmer.accounts.utils.Constants

data class CategoryModel(
    var category: String,
    var type: String,
    @DrawableRes
    val icon: Int
) {
    companion object {
        val incomeBonus: CategoryModel = CategoryModel(
            category = "Bonus",
            type = Constants.TYPE_INCOME,
            icon = R.drawable.ic_inc_bonus
        )
        val incomeDividend: CategoryModel = CategoryModel(
            category = "Dividend",
            type = Constants.TYPE_INCOME,
            icon = R.drawable.ic_inc_dividend
        )
        val incomeGifts: CategoryModel = CategoryModel(
            category = "Gifts",
            type = Constants.TYPE_INCOME,
            icon = R.drawable.ic_inc_gifts
        )
        val incomeInterest: CategoryModel = CategoryModel(
            category = "Interest",
            type = Constants.TYPE_INCOME,
            icon = R.drawable.ic_inc_interest
        )
        val incomeOthers: CategoryModel = CategoryModel(
            category = "Others",
            type = Constants.TYPE_INCOME,
            icon = R.drawable.ic_inc_others
        )
        val incomeRefunds: CategoryModel = CategoryModel(
            category = "Refunds",
            type = Constants.TYPE_INCOME,
            icon = R.drawable.ic_inc_refunds
        )
        val incomeRental: CategoryModel = CategoryModel(
            category = "Rental",
            type = Constants.TYPE_INCOME,
            icon = R.drawable.ic_inc_rental
        )
        val incomeSalary: CategoryModel = CategoryModel(
            category = "Salary",
            type = Constants.TYPE_INCOME,
            icon = R.drawable.ic_inc_salary
        )
        val incomeSales: CategoryModel = CategoryModel(
            category = "Sales",
            type = Constants.TYPE_INCOME,
            icon = R.drawable.ic_inc_sales
        )
        val incomeTip: CategoryModel = CategoryModel(
            category = "Tip",
            type = Constants.TYPE_INCOME,
            icon = R.drawable.ic_inc_tip
        )

        val expenseAdvertising: CategoryModel = CategoryModel(
            category = "Advertising",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_advertising,
        )
        val expenseAnniversary: CategoryModel = CategoryModel(
            category = "Anniversary",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_anniversary,
        )
        val expenseBirthday: CategoryModel = CategoryModel(
            category = "Birthday",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_birthday,
        )
        val expenseCharity: CategoryModel = CategoryModel(
            category = "Charity",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_charity,
        )
        val expenseChristmas: CategoryModel = CategoryModel(
            category = "Christmas",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_christmas,
        )
        val expenseCloths: CategoryModel = CategoryModel(
            category = "Cloths",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_cloths,
        )
        val expenseConcert: CategoryModel = CategoryModel(
            category = "Concert",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_concert,
        )
        val expenseEducation: CategoryModel = CategoryModel(
            category = "Education",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_education,
        )
        val expenseElectricity: CategoryModel = CategoryModel(
            category = "Electricity",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_electricity,
        )
        val expenseEntertainment: CategoryModel = CategoryModel(
            category = "Entertainment",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_entertainment,
        )
        val expenseFees: CategoryModel = CategoryModel(
            category = "Fees",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_fees,
        )
        val expenseFood: CategoryModel = CategoryModel(
            category = "Food",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_food,
        )
        val expenseFurniture: CategoryModel = CategoryModel(
            category = "Furniture",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_furniture,
        )
        val expenseGame: CategoryModel = CategoryModel(
            category = "Game",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_game,
        )
        val expenseGarbage: CategoryModel = CategoryModel(
            category = "Garbage",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_garbage,
        )
        val expenseGrocery: CategoryModel = CategoryModel(
            category = "Grocery",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_grocery,
        )
        val expenseHealthcare: CategoryModel = CategoryModel(
            category = "Healthcare",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_healthcare,
        )
        val expenseHousing: CategoryModel = CategoryModel(
            category = "Housing",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_housing,
        )
        val expenseInsurance: CategoryModel = CategoryModel(
            category = "Insurance",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_insurance,
        )
        val expenseInternet: CategoryModel = CategoryModel(
            category = "Internet",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_internet,
        )
        val expenseInvesting: CategoryModel = CategoryModel(
            category = "Investing",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_investing,
        )
        val expenseLoan: CategoryModel = CategoryModel(
            category = "Loan",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_loan,
        )
        val expenseMaintenance: CategoryModel = CategoryModel(
            category = "Maintenance",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_maintenance,
        )
        val expenseMarketing: CategoryModel = CategoryModel(
            category = "Marketing",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_marketing,
        )
        val expenseMovies: CategoryModel = CategoryModel(
            category = "Movies",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_movies,
        )
        val expenseOffice: CategoryModel = CategoryModel(
            category = "Office",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_office,
        )
        val expenseOther: CategoryModel = CategoryModel(
            category = "Other",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_inc_others,
        )
        val expensePhone: CategoryModel = CategoryModel(
            category = "Phone",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_phone,
        )
        val expenseRent: CategoryModel = CategoryModel(
            category = "Rent",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_rent,
        )
        val expenseRestaurant: CategoryModel = CategoryModel(
            category = "Restaurant",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_restaurant,
        )
        val expenseSalary: CategoryModel = CategoryModel(
            category = "Salary",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_salary,
        )
        val expenseSaving: CategoryModel = CategoryModel(
            category = "Saving",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_saving,
        )
        val expenseShoes: CategoryModel = CategoryModel(
            category = "Shoes",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_shoes,
        )
        val expenseShopping: CategoryModel = CategoryModel(
            category = "Shopping",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_shopping,
        )
        val expenseSubscriptions: CategoryModel = CategoryModel(
            category = "Subscriptions",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_subscriptions,
        )
        val expenseTax: CategoryModel = CategoryModel(
            category = "Tax",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_tax,
        )
        val expenseTransportation: CategoryModel = CategoryModel(
            category = "Transportation",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_transportation,
        )
        val expenseTravel: CategoryModel = CategoryModel(
            category = "Travel",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_travel,
        )
        val expenseUtilities: CategoryModel = CategoryModel(
            category = "Utilities",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_utilities,
        )
        val expenseWater: CategoryModel = CategoryModel(
            category = "Water",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_water,
        )
        val expenseWedding: CategoryModel = CategoryModel(
            category = "Wedding",
            type = Constants.TYPE_EXPENSE,
            icon = R.drawable.ic_exp_wedding,
        )

        val listExpense: List<CategoryModel> by lazy {
            listOf(
                expenseAdvertising, expenseAnniversary, expenseBirthday, expenseCharity,
                expenseChristmas, expenseCloths, expenseConcert, expenseEducation,
                expenseElectricity, expenseEntertainment, expenseFees, expenseFood,
                expenseFurniture, expenseGame, expenseGarbage, expenseGrocery, expenseHealthcare,
                expenseHousing, expenseInsurance, expenseInternet, expenseInvesting, expenseLoan,
                expenseMaintenance, expenseMarketing, expenseMovies, expenseOffice, expenseOther,
                expensePhone, expenseRent, expenseRestaurant, expenseSalary, expenseSaving,
                expenseShoes, expenseShopping, expenseSubscriptions, expenseTax,
                expenseTransportation, expenseTravel, expenseUtilities, expenseWater, expenseWedding
            )
        }

        val listIncome: List<CategoryModel> by lazy {
            listOf(
                incomeBonus, incomeDividend, incomeGifts, incomeInterest, incomeOthers,
                incomeRefunds, incomeRental, incomeSalary, incomeSales, incomeTip
            )
        }

        fun getExpenseIconByTitle(title: String): Int {
            return listExpense.find { it.category == title }?.icon ?: expenseOther.icon
        }

        fun getIncomeIconByTitle(title: String): Int {
            return listIncome.find { it.category == title }?.icon ?: incomeOthers.icon
        }
    }
}