package com.rose.account.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import com.rose.account.R
import com.rose.account.utils.HelperFunctions

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
fun NavShape(credit: Double, debit: Double) {
    val mAmountBalance = HelperFunctions.getRoundedValue((credit - debit))
    val mAmountCredit = HelperFunctions.getRoundedValue(credit)
    val mAmountDebit = HelperFunctions.getRoundedValue(debit)
    val mContentPadding = 5.dp
    val mCornerDp = 100.dp
    val mFirstRowWeight = 1.5f
    val mSecondRowWeight = 2f
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
                    Image(
                        modifier = Modifier
                            .padding(top = mContentPadding)
                            .size(size = 48.dp),
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = stringResource(R.string.content_description_app_logo)
                    )
                    Text(
                        modifier = Modifier.padding(top = mContentPadding),
                        text = stringResource(id = R.string.app_name),
                        fontWeight = FontWeight.W900,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    )
                    ElevatedCard(
                        modifier = Modifier
                            .padding(
                                top = mContentPadding,
                                start = mContentPadding,
                                end = mContentPadding
                            )
                            .size(width = 200.dp, height = 75.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Row(Modifier.padding(start = mContentPadding, end = mContentPadding)) {
                            TableCell(
                                stringResource(id = R.string.label_nav_credit), mFirstRowWeight
                            )
                            TableCell(mAmountCredit, mSecondRowWeight)
                        }
                        Row(Modifier.padding(start = mContentPadding, end = mContentPadding)) {
                            TableCell(
                                stringResource(id = R.string.label_nav_debit), mFirstRowWeight
                            )
                            TableCell(mAmountDebit, mSecondRowWeight)
                        }
                        Row(Modifier.padding(start = mContentPadding, end = mContentPadding)) {
                            Divider(
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                thickness = 2.dp
                            )
                        }
                        Row(Modifier.padding(start = mContentPadding, end = mContentPadding)) {
                            TableCell(
                                stringResource(id = R.string.label_balance), mFirstRowWeight, true
                            )
                            TableCell(mAmountBalance, mSecondRowWeight, true)
                        }
                    }
                }
            }
        }
    }
}