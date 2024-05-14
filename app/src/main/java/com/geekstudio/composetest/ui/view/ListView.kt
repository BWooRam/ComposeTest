package com.geekstudio.composetest.ui.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geekstudio.composetest.data.dto.ContactVal
import com.geekstudio.composetest.data.dto.ContactVar
import com.geekstudio.composetest.data.dto.ContactsVal
import com.geekstudio.composetest.data.dto.ContactsVar
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
fun ContactList(contacts: ContactsVal) {
    val padding = PaddingValues(horizontal = DefaultMargin, vertical = 8.dp)
    LazyColumn(
        contentPadding = padding
    ) {
        itemsIndexed(
            contacts.contacts
        ) { _, item ->
            Log.d("ContactItemCard", "ContactList ContactsVal execute item = $item")
            ContactItemCard(item)
        }
    }
}

@Composable
fun ContactList(contacts: ContactsVar) {
    val padding = PaddingValues(horizontal = DefaultMargin, vertical = 8.dp)
    LazyColumn(
        contentPadding = padding
    ) {
        itemsIndexed(
            contacts.contacts
        ) { _, item ->
            Log.d("ContactItemCard", "ContactList ContactsVar execute item = $item")
            ContactItemCard(item)
        }
    }
}

@Composable
fun ContactItemCard(item: ContactVal) {
    var selected by remember { mutableStateOf(false) }
    val backgroundColor = when (selected) {
        true -> Color.LightGray
        else -> Color.DarkGray
    }
    Log.d("ContactItemCard", "ContactVal item = $item, backgroundColor = $backgroundColor")
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { selected = !selected },
        elevation = 2.dp,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = DefaultMargin, vertical = DefaultMargin),
        ) {
            Text(
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                text = Util.decodingHtml(item.name),
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
                text = Util.decodingHtml(item.number),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ContactItemCard(item: ContactVar) {
    var selected by remember { mutableStateOf(false) }
    val backgroundColor = when (selected) {
        true -> Color.LightGray
        else -> Color.DarkGray
    }
    Log.d("ContactItemCard", "ContactVar item = $item, backgroundColor = $backgroundColor")
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { selected = !selected },
        elevation = 2.dp,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = DefaultMargin, vertical = DefaultMargin),
        ) {
            Text(
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                text = Util.decodingHtml(item.name),
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
                text = Util.decodingHtml(item.number),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ItemCard(item: Item, onClick: () -> Unit) {
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
