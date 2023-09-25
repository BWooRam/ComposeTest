package com.geekstudio.composetest.presentation.main

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.geekstudio.composetest.data.dto.Rss
import com.geekstudio.composetest.presentation.base.BaseActivity
import com.geekstudio.composetest.presentation.base.BaseUiState
import com.geekstudio.composetest.ui.theme.ComposeTestTheme
import com.geekstudio.composetest.ui.view.RssList
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileInputStream

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val titleState = mutableStateOf("")
    private val valueState = mutableStateOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView(null)
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
                                Log.d("initUiObserver", "data = ${it.data}")
                                titleState.value = "Success Title"
                                valueState.value = "Success Value"
                                initView(it.data)
                            }

                            else -> {

                            }
                        }
                    }

                    is BaseUiState.Loading -> {
                        Log.d("initUiObserver", "loading")
                        titleState.value = "Loading Title"
                        valueState.value = "Loading Value"
                    }

                    is BaseUiState.Error -> {
                        Log.d("initUiObserver", "Error = ${it.error}")
                        titleState.value = "Error Title"
                        valueState.value = "Error Value"
                    }
                }
            }
        }
    }

    private fun initView(rss: Rss?) {
        setContent {
            ComposeTestTheme {
                val titleRemember = remember { titleState }
                val valueRemember = remember { valueState }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        Text(text = titleRemember.value)
                        Text(text = valueRemember.value)
                        Button(onClick = { viewModel.loadNewsRss() }) {
                            Text(text = "refresh")
                        }

                        if (rss != null)
                            RssList(this@MainActivity, rss)
                    }
                }
            }
        }
    }
}

