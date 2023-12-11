package com.ahmer.accounts.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ahmer.accounts.R

sealed class NavItems(
    val label: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val contentDescription: Int,
    val route: String,
    val argument: String,
) {
    data object Accounts : NavItems(
        label = R.string.label_accounts,
        selectedIcon = R.drawable.ic_filled_accounts,
        unselectedIcon = R.drawable.ic_outlined_accounts,
        contentDescription = R.string.content_description_accounts,
        route = "mainScreen",
        argument = "",
    ) {
        val fullRoute = route + argument
    }

    data object Expense : NavItems(
        label = R.string.label_expenses,
        selectedIcon = R.drawable.ic_filled_expense,
        unselectedIcon = R.drawable.ic_outlined_expense,
        contentDescription = R.string.content_description_expenses,
        route = "expenseScreen",
        argument = "",
    ) {
        val fullRoute = route + argument
    }

    data object Report : NavItems(
        label = R.string.label_report,
        selectedIcon = R.drawable.ic_filled_report,
        unselectedIcon = R.drawable.ic_outlined_report,
        contentDescription = R.string.content_description_report,
        route = "reportScreen",
        argument = ""
    ) {
        val fullRoute = route + argument
    }

    data object Settings : NavItems(
        label = R.string.label_settings,
        selectedIcon = R.drawable.ic_filled_settings,
        unselectedIcon = R.drawable.ic_outlined_settings,
        contentDescription = R.string.content_description_settings,
        route = "settingsScreen",
        argument = ""
    ) {
        val fullRoute = route + argument
    }

    data object PersonAddEdit : NavItems(
        label = R.string.label_person_add_edit,
        selectedIcon = R.drawable.ic_filled_settings,
        unselectedIcon = R.drawable.ic_outlined_settings,
        contentDescription = R.string.content_description_person_add_edit,
        route = "personAddEditScreen",
        argument = "?personId={personId}"
    ) {
        val fullRoute = route + argument
    }

    data object Transactions : NavItems(
        label = R.string.label_trans,
        selectedIcon = R.drawable.ic_filled_settings,
        unselectedIcon = R.drawable.ic_outlined_settings,
        contentDescription = R.string.content_description_trans,
        route = "transScreen",
        argument = "?transPersonId={transPersonId}"
    ) {
        val fullRoute = route + argument
    }

    data object TransactionsAddEdit : NavItems(
        label = R.string.label_trans_add_edit,
        selectedIcon = R.drawable.ic_filled_settings,
        unselectedIcon = R.drawable.ic_outlined_settings,
        contentDescription = R.string.content_description_trans_add_edit,
        route = "transAddEditScreen",
        argument = "?transId={transId}/transPersonId={transPersonId}"
    ) {
        val fullRoute = route + argument
    }

    companion object {
        val navBottomItems: List<NavItems> = listOf(Accounts, Report, Settings)

        @Composable
        fun NavItems.Icons(selected: Boolean) {
            Icon(
                painter = painterResource(id = if (selected) selectedIcon else unselectedIcon),
                contentDescription = stringResource(id = contentDescription)
            )
        }
    }
}


