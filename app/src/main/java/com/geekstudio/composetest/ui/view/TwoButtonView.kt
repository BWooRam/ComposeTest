package com.geekstudio.composetest.ui.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
fun TwoButtonCard(
    onClick1: () -> Unit,
    onClick2: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.background,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = DefaultMargin, vertical = DefaultMargin),
        ) {
            Button(
                onClick = onClick1,
                modifier = Modifier.wrapContentSize()
            ) { Text(text = "Setting isDarkTheme Change!") }
            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
            )
            Button(
                onClick = onClick2,
                modifier = Modifier.wrapContentSize()
            ) { Text(text = "Test Value Change!") }
        }
    }
}
