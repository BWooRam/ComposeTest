package com.geekstudio.composetest.presentation.test

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
    private val titleState = mutableStateOf("")
    private val valueState = mutableStateOf("")
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
                        val ui by viewModel.uiSharedFlow.collectAsState(BaseUiState.Loading)
                        Log.d("RssList", "initView ui = $ui")
                        if (ui is BaseUiState.Success<*>) {
                            runCatching {
                                (ui as BaseUiState.Success<*>).data as Rss
                            }.onSuccess { rss ->
                                RssList(this@TestActivity, rss)
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
                                titleState.value = "Success Title"
                                valueState.value = "Success Value"
                            }

                            else -> {

                            }
                        }
                    }

                    is BaseUiState.Loading -> {
                        Log.d("RssList", "initUiObserver loading")
                        titleState.value = "Loading Title"
                        valueState.value = "Loading Value"
                    }

                    is BaseUiState.Error -> {
                        Log.d("RssList", "initUiObserver Error = ${it.error}")
                        titleState.value = "Error Title"
                        valueState.value = "Error Value"
                    }
                }
            }
        }
    }
}

