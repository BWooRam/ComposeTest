package com.geekstudio.composetest.presentation.test

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.geekstudio.composetest.data.dto.Rss
import com.geekstudio.composetest.presentation.base.BaseActivity
import com.geekstudio.composetest.presentation.base.BaseUiState
import com.geekstudio.composetest.ui.theme.ComposeTestTheme
import com.geekstudio.composetest.ui.view.RssList
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class TestActivity : BaseActivity() {
    private val viewModel: TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
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
                        val ui by viewModel.uiSharedFlow.collectAsState(BaseUiState.Loading)
                        Log.d("RssList", "initView ui = $ui")
                        when (ui) {
                            is BaseUiState.Success<*> -> {
                                runCatching {
                                    (ui as BaseUiState.Success<*>).data as Rss
                                }.onSuccess { rss ->
                                    RssList(this@TestActivity, rss)
                                }
                            }

                            is BaseUiState.Loading -> {
                                Text(
                                    text = "Loading",
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .align(Alignment.CenterHorizontally)
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
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        initUiObserver()
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

