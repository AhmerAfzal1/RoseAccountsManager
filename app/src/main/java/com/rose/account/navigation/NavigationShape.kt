package com.rose.account.navigation

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rose.account.utils.HelperFunctions
import com.rose.account.R

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
    val mCardPadding = 32.dp
    val mContentPadding = 5.dp
    val mCornerDp = 100.dp
    val mFirstRowWeight = 0.3f
    val mSecondRowWeight = 0.7f
    val mTextOffset = Offset(4.0f, 8.0f)
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
                        modifier = Modifier.size(size = 48.dp),
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = stringResource(R.string.content_description_app_logo)
                    )
                    Text(
                        modifier = Modifier.padding(top = mContentPadding),
                        text = stringResource(id = R.string.app_name),
                        style = TextStyle(
                            fontSize = 18.sp,
                            shadow = Shadow(
                                color = Color.Black, offset = mTextOffset, blurRadius = 3f
                            )
                        ),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    Card(
                        modifier = Modifier.padding(
                            top = mContentPadding, start = mCardPadding, end = mCardPadding
                        ),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
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
                            Divider(color = Color.Black, thickness = 2.dp)
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