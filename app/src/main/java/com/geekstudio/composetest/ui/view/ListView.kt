package com.geekstudio.composetest.ui.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geekstudio.composetest.data.dto.Item
import com.geekstudio.composetest.data.dto.Rss
import com.geekstudio.composetest.ui.theme.DefaultMargin
import com.geekstudio.composetest.utils.Util

@Composable
fun RssList(context: Context, rss: Rss) {
    val channel = rss.channel
    val padding = PaddingValues(horizontal = DefaultMargin, vertical = 8.dp)
    Log.d("ListView", "RssList execute")
    LazyColumn(
        contentPadding = padding
    ) {
        itemsIndexed(
            channel!!.item
        ) { _, item ->
            ItemCard(item) {
                Toast.makeText(context, "RssList onClick item = $item", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
private fun ItemCard(item: Item, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.background,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = DefaultMargin, vertical = DefaultMargin),
        ) {
            Text(
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                text = Util.decodingHtml(item.title),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
            )
            Text(
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Start,
                maxLines = 2,
                text = Util.decodingHtml(item.description),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
