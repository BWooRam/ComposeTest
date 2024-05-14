package com.geekstudio.composetest.presentation.test

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.geekstudio.composetest.data.dto.ContactVal
import com.geekstudio.composetest.data.dto.ContactVar
import com.geekstudio.composetest.data.dto.Rss
import com.geekstudio.composetest.presentation.base.BaseActivity
import com.geekstudio.composetest.presentation.base.BaseUiState
import com.geekstudio.composetest.ui.theme.ComposeTestTheme
import com.geekstudio.composetest.ui.view.ContactItemCard
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class TestActivity3 : BaseActivity() {
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
                            Log.d("ContactItemCard", "initView randomSeed = $randomSeed")
                            when (randomSeed) {
                                0 -> {
                                    Log.d("ContactItemCard", "initView showRandomContactValUi")
                                    viewModel.showRandomContactValUi()
                                }

                                1 -> {
                                    Log.d("ContactItemCard", "initView showRandomContactVarUi")
                                    viewModel.showRandomContactVarUi()
                                }
                            }
                        }) {
                            Text(text = "refresh")
                        }
                        Log.d("ContactItemCard", "initView start")
                        val ui by viewModel.uiSharedFlow.collectAsState(initial = BaseUiState.Loading)

                        if (ui is BaseUiState.Success<*>) {
                            runCatching {
                                (ui as BaseUiState.Success<*>).data
                            }.onSuccess { data ->
                                when (data) {
                                    is ContactVar -> ContactItemCard(data)
                                    is ContactVal -> ContactItemCard(data)
                                }
                            }
                        }
                    }
                }
            }
            initUiObserver()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.showRandomContactValUi()
//        viewModel.showRandomContactVarUi()
    }

    private fun initUiObserver() {
        repeatOnStarted {
            viewModel.uiSharedFlow.collect {
                when (it) {
                    is BaseUiState.Success<*> -> {
                        when (it.data) {
                            is Rss -> {
                                Log.d("RssList", "initUiObserver Rss = ${it.data}")
                            }

                            is ContactVar -> {
                                Log.d("RssList", "initUiObserver ContactVar = ${it.data}")
                            }

                            is ContactVal -> {
                                Log.d("RssList", "initUiObserver ContactVal = ${it.data}")
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

