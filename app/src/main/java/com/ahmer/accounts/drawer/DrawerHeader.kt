package com.ahmer.accounts.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.utils.HelperUtils

@Composable
fun RowScope.TableCell(text: String, weight: Float, isBold: Boolean = false) {
    if (!isBold) {
        Text(
            modifier = Modifier
                .weight(weight)
                .padding(2.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall
        )
    } else {
        Text(
            modifier = Modifier
                .weight(weight)
                .padding(3.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NavShape(transSumModel: TransSumModel) {
    val mContentPadding = 5.dp
    val mCornerDp = 100.dp
    val mTotalCredit: Double = transSumModel.creditSum?.toDouble() ?: 0.0
    val mTotalDebit: Double = transSumModel.debitSum?.toDouble() ?: 0.0
    val mTotalBalance = HelperUtils.getRoundedValue((mTotalCredit.minus(mTotalDebit)))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically)
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = mCornerDp, bottomEnd = mCornerDp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = mContentPadding)
            ) {
                item {
                    Content(
                        contentPadding = mContentPadding, totalCredit = mTotalCredit,
                        totalDebit = mTotalDebit, totalBalance = mTotalBalance
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    contentPadding: Dp, totalCredit: Double, totalDebit: Double, totalBalance: String
) {
    val mFirstRowWeight = 1.5f
    val mSecondRowWeight = 2f

    Image(
        modifier = Modifier
            .padding(top = contentPadding)
            .size(size = 48.dp),
        painter = painterResource(id = R.drawable.ic_main),
        contentDescription = stringResource(R.string.content_description_app_logo)
    )
    Text(
        modifier = Modifier.padding(top = contentPadding),
        text = stringResource(id = R.string.app_name),
        fontWeight = FontWeight.W900,
        color = MaterialTheme.colorScheme.secondaryContainer,
    )
    ElevatedCard(
        modifier = Modifier
            .padding(
                top = contentPadding,
                start = contentPadding,
                end = contentPadding
            )
            .size(width = 200.dp, height = 85.dp),
        shape = RoundedCornerShape(size = 10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.padding(start = contentPadding, end = contentPadding)) {
                TableCell(stringResource(id = R.string.label_nav_credit), mFirstRowWeight)
                TableCell(totalCredit.toString(), mSecondRowWeight)
            }
            Row(Modifier.padding(start = contentPadding, end = contentPadding)) {
                TableCell(stringResource(id = R.string.label_nav_debit), mFirstRowWeight)
                TableCell(totalDebit.toString(), mSecondRowWeight)
            }
            Row(Modifier.padding(start = contentPadding, end = contentPadding)) {
                Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Row(Modifier.padding(start = contentPadding, end = contentPadding)) {
                TableCell(
                    stringResource(id = R.string.label_balance), mFirstRowWeight, isBold = true
                )
                TableCell(totalBalance, mSecondRowWeight, isBold = true)
            }
        }
    }
}