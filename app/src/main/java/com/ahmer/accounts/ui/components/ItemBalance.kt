package com.ahmer.accounts.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.database.model.TransactionSumModel
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorGreenLight
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.ui.theme.colorRedLight
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.DeleteIcon
import com.ahmer.accounts.utils.EditIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.InfoIcon
import com.ahmer.accounts.utils.PdfIcon


/**
 * Data class for balance information
 */
data class BalanceData(
    val transactionSum: TransactionSumModel,
    val currency: Currency
)

/**
 * Sealed class for balance actions
 */
sealed class BalanceAction {
    data object Delete : BalanceAction()
    data object Info : BalanceAction()
    data object Pdf : BalanceAction()
    data object Edit : BalanceAction()
}

/**
 * Displays a balance card with optional person details and actions.
 *
 * @param modifier Modifier for styling
 * @param balanceData Consolidated balance information
 * @param person Optional person details to display
 * @param onAction Callback for user actions
 */
@Composable
fun ItemBalance(
    modifier: Modifier = Modifier,
    balanceData: BalanceData,
    person: PersonEntity? = null,
    onAction: (BalanceAction) -> Unit = {}
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.primary
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            person?.let {
                PersonDetailsSection(
                    person = it,
                    onAction = onAction,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            BalanceCardsSection(
                transactionSum = balanceData.transactionSum,
                currency = balanceData.currency,
                padding = if (person != null) PaddingValues(horizontal = 8.dp)
                else PaddingValues(horizontal = 8.dp, vertical = 6.dp)
            )
        }
    }
}

/**
 * Displays person details and action buttons
 */
@Composable
private fun PersonDetailsSection(
    modifier: Modifier = Modifier,
    person: PersonEntity,
    onAction: (BalanceAction) -> Unit
) {
    val isActionsEnabled = person.name.isNotEmpty()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = person.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (person.phone.isNotEmpty()) {
                Text(
                    text = person.phone,
                    modifier = Modifier.padding(bottom = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        ActionButtons(
            isEnabled = isActionsEnabled,
            onDelete = { onAction(BalanceAction.Delete) },
            onInfo = { onAction(BalanceAction.Info) },
            onPdf = { onAction(BalanceAction.Pdf) },
            onEdit = { onAction(BalanceAction.Edit) }
        )
    }
}

/**
 * Reusable action button component
 */
@Composable
private fun ActionIconButton(
    onClick: () -> Unit,
    enabled: Boolean,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(Constants.ICON_SIZE - 6.dp),
        enabled = enabled
    ) {
        icon()
    }
}

/**
 * Displays action buttons in a row
 */
@Composable
private fun ActionButtons(
    isEnabled: Boolean,
    onDelete: () -> Unit,
    onInfo: () -> Unit,
    onPdf: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        ActionIconButton(
            onClick = onDelete,
            enabled = isEnabled,
            icon = { DeleteIcon(tint = MaterialTheme.colorScheme.error) }
        )
        ActionIconButton(
            onClick = onInfo,
            enabled = isEnabled,
            icon = { InfoIcon() }
        )
        ActionIconButton(
            onClick = onPdf,
            enabled = isEnabled,
            icon = { PdfIcon() }
        )
        ActionIconButton(
            onClick = onEdit,
            enabled = isEnabled,
            icon = { EditIcon() }
        )
    }
}

/**
 * Displays balance cards in a column
 */
@Composable
private fun BalanceCardsSection(
    transactionSum: TransactionSumModel,
    currency: Currency,
    padding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BalanceCard(
                modifier = Modifier.weight(1f),
                amount = transactionSum.creditSum,
                label = stringResource(R.string.label_total) + " ${Constants.TYPE_CREDIT}",
                textColor = colorGreenDark,
                backgroundColor = colorGreenLight,
                currency = currency
            )

            BalanceCard(
                modifier = Modifier.weight(1f),
                amount = transactionSum.debitSum,
                label = stringResource(R.string.label_total) + " ${Constants.TYPE_DEBIT}",
                textColor = colorRedDark,
                backgroundColor = colorRedLight,
                currency = currency
            )
        }

        BalanceCard(
            modifier = Modifier.padding(padding),
            amount = transactionSum.balance,
            label = stringResource(R.string.label_total_balance),
            textColor = if (transactionSum.balance >= 0) {
                colorGreenDark
            } else {
                colorRedDark
            },
            backgroundColor = if (transactionSum.balance >= 0) {
                colorGreenLight
            } else {
                colorRedLight
            },
            currency = currency
        )
    }
}

@Composable
private fun BalanceCard(
    modifier: Modifier = Modifier,
    currency: Currency,
    textColor: Color,
    backgroundColor: Color,
    amount: Double,
    label: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .shadow(elevation = 0.5.dp, shape = RoundedCornerShape(size = 1.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currency.symbol,
                color = textColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = " ${HelperUtils.roundValue(value = amount)}",
                color = textColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Text(
            text = label.uppercase(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            color = textColor,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}