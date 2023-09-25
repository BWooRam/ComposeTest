package com.geekstudio.composetest.presentation.datastore

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.geekstudio.composetest.presentation.base.BaseActivity
import com.geekstudio.composetest.ui.theme.ComposeTestTheme
import com.geekstudio.composetest.ui.view.TwoButtonCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataStoreActivity : BaseActivity() {
    private val viewModel: DataStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initUiObserver()
    }

    private fun initUiObserver() {
        repeatOnStarted {
            /*viewModel.isDarkThemeFlow.collect {
                Log.d("DataStoreActivity", "initUiObserver SettingData = $it")
                viewModel.currentSettingData = it
            }*/

            viewModel.testDataFlow.collect {
                Log.d("DataStoreActivity", "initUiObserver isTest = ${it.isTest}")
                viewModel.currentTestData = it
            }
        }
    }

    private fun initView() {
        setContent {
            ComposeTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TwoButtonCard({
                        if (viewModel.currentSettingData != null) {
                            val isDarkTheme = viewModel.currentSettingData?.isDarkTheme ?: false
                            viewModel.updateDarkTheme(!isDarkTheme)
                        }
                    }, {
                        Log.d(
                            "DataStoreActivity",
                            "test button currentTestData = ${viewModel.currentTestData?.isTest}"
                        )
                        if (viewModel.currentTestData != null) {
                            val isTest = viewModel.currentTestData?.isTest ?: false
                            viewModel.updateTest(!isTest)
                        }
                    })
                }
            }
        }
    }
}

