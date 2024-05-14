package com.geekstudio.composetest.presentation.test

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.geekstudio.composetest.data.dto.Rss
import com.geekstudio.composetest.presentation.base.BaseActivity
import com.geekstudio.composetest.presentation.base.BaseUiState
import com.geekstudio.composetest.ui.theme.ComposeTestTheme
import com.geekstudio.composetest.ui.theme.DefaultMargin
import com.geekstudio.composetest.ui.view.ItemCard
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class TestActivity2 : BaseActivity() {
    private val viewModel: TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Button(onClick = {
                            val randomSeed = Random.nextInt(0, 2)
                            Log.d("RssList", "initView randomSeed = $randomSeed")
                            when (randomSeed) {
                                0 -> {
                                    Log.d("RssList", "initView Error")
                                    viewModel.showRandomErrorUi()
                                }

                                1 -> {
                                    Log.d("RssList", "initView loadNewsRss")
                                    viewModel.loadNewsRss()
                                }
                            }
                        }) {
                            Text(text = "refresh")
                        }
                        Log.d("RssList", "initView start")
                        RssListText()
                        RssList()
                    }
                }
            }
            initUiObserver()
        }
    }

    @Composable
    fun RssListText() {
        Log.d("RssList", "RssListText initView")
        Text(text = "RssList")
    }

    @Composable
    fun RssList() {
        val ui by viewModel.uiSharedFlow.collectAsState(BaseUiState.Loading)
        Log.d("RssList", "RssList ui = $ui")
        when (ui) {
            is BaseUiState.Success<*> -> {
                runCatching {
                    (ui as BaseUiState.Success<*>).data as Rss
                }.onSuccess { rss ->
                    val channel = rss.channel
                    val padding = PaddingValues(horizontal = DefaultMargin, vertical = 8.dp)
                    Log.d("RssList", "RssList execute")
                    LazyColumn(
                        contentPadding = padding
                    ) {
                        itemsIndexed(
                            channel!!.item
                        ) { _, item ->
                            ItemCard(item) {
                                Toast.makeText(
                                    this@TestActivity2,
                                    "RssList onClick item = $item",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            is BaseUiState.Loading -> {
                Text(
                    text = "Loading",
                    modifier = Modifier
                        .wrapContentSize()
                )
            }

            is BaseUiState.Error -> {
                runCatching {
                    (ui as BaseUiState.Error).error
                }.onSuccess { error ->
                    Text(
                        text = error?.message ?: "",
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadNewsRss()
    }

    private fun initUiObserver() {
        repeatOnStarted {
            viewModel.uiSharedFlow.collect {
                when (it) {
                    is BaseUiState.Success<*> -> {
                        when (it.data) {
                            is Rss -> {
                                Log.d("RssList", "initUiObserver data = ${it.data}")
                            }

                            else -> {

                            }
                        }
                    }

                    is BaseUiState.Loading -> {
                        Log.d("RssList", "initUiObserver loading")
                    }

                    is BaseUiState.Error -> {
                        Log.d("RssList", "initUiObserver Error = ${it.error}")
                    }
                }
            }
        }
    }
}

