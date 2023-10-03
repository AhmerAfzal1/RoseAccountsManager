package com.ahmer.accounts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorGreenLight
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.ui.theme.colorRedLight
import com.ahmer.accounts.utils.CreditIcon
import com.ahmer.accounts.utils.DebitIcon
import com.ahmer.accounts.utils.HelperUtils

@Composable
fun TransTotal(transSumModel: TransSumModel) {
    val mColorBackground: Color
    val mColorText: Color
    val mCredit: Double = transSumModel.creditSum?.toDouble() ?: 0.0
    val mDebit: Double = transSumModel.debitSum?.toDouble() ?: 0.0
    val mHorizontalSpace: Dp = 2.dp
    val mRoundShapeSize: Dp = 10.dp
    val mTotalBalance: Double = mCredit.minus(mDebit)

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
        Row {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .weight(weight = 1f)
                    .clip(shape = RoundedCornerShape(size = mRoundShapeSize))
                    .padding(horizontal = mHorizontalSpace)
                    .background(color = colorGreenLight)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CreditIcon(
                        modifier = Modifier
                            .size(size = 30.dp)
                            .padding(top = 5.dp, end = 5.dp)
                            .align(alignment = Alignment.End),
                        tint = colorGreenDark
                    )
                    Text(
                        text = stringResource(id = R.string.label_total_credit),
                        color = colorGreenDark,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = HelperUtils.getRoundedValue(mCredit),
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
                    .weight(weight = 1f)
                    .clip(shape = RoundedCornerShape(size = mRoundShapeSize))
                    .padding(horizontal = mHorizontalSpace)
                    .background(color = colorRedLight)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DebitIcon(
                        modifier = Modifier
                            .size(size = 30.dp)
                            .padding(top = 5.dp, end = 5.dp)
                            .align(alignment = Alignment.End),
                        tint = colorRedDark
                    )
                    Text(
                        text = stringResource(id = R.string.label_total_debit),
                        color = colorRedDark,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = HelperUtils.getRoundedValue(mDebit),
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
                    .weight(weight = 1f)
                    .clip(shape = RoundedCornerShape(size = mRoundShapeSize))
                    .padding(horizontal = mHorizontalSpace)
                    .background(color = mColorBackground)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (mTotalBalance >= 0) {
                        CreditIcon(
                            modifier = Modifier
                                .size(size = 30.dp)
                                .padding(top = 5.dp, end = 5.dp)
                                .align(alignment = Alignment.End),
                            tint = colorGreenDark
                        )
                    } else {
                        DebitIcon(
                            modifier = Modifier
                                .size(size = 30.dp)
                                .padding(top = 5.dp, end = 5.dp)
                                .align(alignment = Alignment.End),
                            tint = colorRedDark
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.label_total_balance),
                        color = mColorText,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = HelperUtils.getRoundedValue(mTotalBalance),
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