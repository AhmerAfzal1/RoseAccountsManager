package com.ahmer.accounts.ui.components

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.database.model.PersonBalanceModel
import com.ahmer.accounts.event.PersonEvent
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.HelperUtils

/**
 * Composable item representing a person with their balance information.
 *
 * @param account Contains person details and their balance information
 * @param currency Currency type for amount display
 * @param onEvent Callback for handling user interactions with the person item
 * @param modifier Modifier for styling and layout adjustments
 */
@Composable
fun ItemPerson(
    account: PersonBalanceModel,
    currency: Currency,
    onEvent: (PersonEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val balance: Double = account.balanceModel.balance
    val context: Context = LocalContext.current
    val person: PersonEntity = account.personsEntity
    val textColor = if (balance >= 0) colorGreenDark else colorRedDark
    val horizontalPadding = 8.dp

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = 6.dp)
                .clickable { onEvent(PersonEvent.OnAddEditTransaction(person)) },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InitialsAvatar(name = person.name)
            Spacer(modifier = Modifier.width(horizontalPadding))

            PersonDetailsSection(
                person = person,
                balance = balance,
                currency = currency,
                context = context,
                textColor = textColor
            )
        }

        HelperUtils.ListDivider(thickness = 0.5.dp, alpha = 0.15f)
    }
}

/**
 * Displays a circular avatar with the person's initials.
 */
@Composable
private fun InitialsAvatar(name: String) {
    Box(
        modifier = Modifier
            .size(size = 40.dp)
            .clip(shape = CircleShape)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.firstOrNull()?.toString() ?: "?",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

/**
 * Displays person details and balance information.
 */
@Composable
private fun PersonDetailsSection(
    person: PersonEntity,
    balance: Double,
    currency: Currency,
    context: Context,
    textColor: Color
) {
    val maxLines = 1
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(weight = 0.6f)) {
            Text(
                text = person.name,
                fontWeight = FontWeight.Bold,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall
            )

            if (person.phone.isNotEmpty()) {
                Text(
                    text = person.phone,
                    color = Color.Gray,
                    maxLines = maxLines,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        HelperUtils.AmountWithSymbolText(
            modifier = Modifier.weight(weight = 0.4f),
            context = context,
            currency = currency,
            amount = balance,
            color = textColor,
            isBold = false
        )
    }
}