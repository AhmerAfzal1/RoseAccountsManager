package com.ahmer.accounts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorGreenLight
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.ui.theme.colorRedLight
import com.ahmer.accounts.utils.HelperFunctions

@Composable
fun TransTotal(transSumModel: TransSumModel) {
    val mColorBackground: Color
    val mColorText: Color
    val mCredit = transSumModel.creditSum?.toDouble() ?: 0.0
    val mDebit = transSumModel.debitSum?.toDouble() ?: 0.0
    val mTotalBalance = mCredit.minus(mDebit)

    if (mTotalBalance >= 0) {
        mColorBackground = colorGreenLight
        mColorText = colorGreenDark
    } else {
        mColorBackground = colorRedLight
        mColorText = colorRedDark
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Row(modifier = Modifier) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(horizontal = 3.dp)
                    .background(colorGreenLight)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CREDIT",
                        modifier = Modifier.padding(top = 5.dp),
                        color = colorGreenDark,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = HelperFunctions.getRoundedValue(mCredit),
                        color = colorGreenDark,
                        modifier = Modifier.padding(bottom = 5.dp),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(horizontal = 3.dp)
                    .background(colorRedLight)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "DEBIT",
                        modifier = Modifier.padding(top = 5.dp),
                        color = colorRedDark,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = HelperFunctions.getRoundedValue(mDebit),
                        color = colorRedDark,
                        modifier = Modifier.padding(bottom = 5.dp),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(horizontal = 3.dp)
                    .background(mColorBackground)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "BALANCE",
                        modifier = Modifier.padding(top = 5.dp),
                        color = mColorText,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = HelperFunctions.getRoundedValue(mTotalBalance),
                        color = mColorText,
                        modifier = Modifier.padding(bottom = 5.dp),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}