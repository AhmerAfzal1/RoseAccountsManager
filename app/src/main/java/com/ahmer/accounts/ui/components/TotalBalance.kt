package com.ahmer.accounts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorGreenLight
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.ui.theme.colorRedLight
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.CreditIcon
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.DebitIcon
import com.ahmer.accounts.utils.HelperUtils

@Composable
fun TotalBalance(
    modifier: Modifier = Modifier,
    transSumModel: TransSumModel,
    currency: Currency,
) {
    val mCreditIcon: @Composable () -> Unit = {
        CreditIcon(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(size = 18.dp),
            tint = colorGreenDark
        )
    }
    val mDebtIcon: @Composable () -> Unit = {
        DebitIcon(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(size = 18.dp),
            tint = colorRedDark
        )
    }

    val mColorBackground: Color
    val mColorText: Color
    val mIcon: @Composable () -> Unit

    if (transSumModel.balance >= 0) {
        mColorBackground = colorGreenLight
        mColorText = colorGreenDark
        mIcon = { mCreditIcon.invoke() }
    } else {
        mColorBackground = colorRedLight
        mColorText = colorRedDark
        mIcon = { mDebtIcon.invoke() }
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp),
        shape = RoundedCornerShape(size = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp, top = 5.dp)
                .background(color = colorGreenLight, shape = RoundedCornerShape(size = 4.dp))
        ) {
            Row(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 1.dp, bottom = 1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total ${Constants.TYPE_CREDIT}".uppercase(),
                    color = colorGreenDark,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall,
                )
                mCreditIcon.invoke()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = currency.symbol,
                        color = colorGreenDark,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = " ${HelperUtils.getRoundedValue(value = transSumModel.creditSum)}",
                        color = colorGreenDark,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp, top = 5.dp)
                .background(color = colorRedLight, shape = RoundedCornerShape(size = 4.dp))
        ) {
            Row(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 1.dp, bottom = 1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total ${Constants.TYPE_DEBIT}".uppercase(),
                    color = colorRedDark,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall,
                )
                mDebtIcon.invoke()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    val mDebit: String = HelperUtils.getRoundedValue(value = transSumModel.debitSum)
                    Text(
                        text = currency.symbol,
                        color = colorRedDark,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = if (transSumModel.debitSum.toInt() == 0) " $mDebit" else " -$mDebit",
                        color = colorRedDark,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 5.dp)
                .background(color = mColorBackground, shape = RoundedCornerShape(size = 4.dp))
        ) {
            Row(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 1.dp, bottom = 1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.label_total_balance),
                    color = mColorText,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall,
                )
                mIcon.invoke()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = currency.symbol,
                        color = mColorText,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = " ${HelperUtils.getRoundedValue(value = transSumModel.balance)}",
                        color = mColorText,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}