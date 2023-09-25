package com.geekstudio.composetest.presentation.mvimain

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.edit
import com.geekstudio.composetest.data.dto.Rss
import com.geekstudio.composetest.data.dto.SettingsData
import com.geekstudio.composetest.data.remote.SettingDataStore
import com.geekstudio.composetest.module.DataStoreModule
import com.geekstudio.composetest.presentation.base.BaseActivity
import com.geekstudio.composetest.ui.theme.ComposeTestTheme
import com.geekstudio.composetest.ui.view.RssList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MviMainActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: MviMainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUiObserver()
        initView(null)

    }

    override fun onStart() {
        super.onStart()
        viewModel.loadNewsRss()
    }

    private fun initUiObserver() {
        repeatOnStarted {
            viewModel.state.collect {
                Log.d("MviMainActivity", "isLoading = ${it.isLoading}, rss = ${it.rss}")
                initView(it.rss)
            }
        }
    }

    private fun initView(rss: Rss?) {
        setContent {
            ComposeTestTheme {
                val titleRemember = remember { mutableStateOf("") }
                val valueRemember = remember { mutableStateOf("") }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.surface
                ) {
                    Column {
                        Text(text = titleRemember.value)
                        Text(text = valueRemember.value)
                        Button(onClick = { viewModel.loadNewsRss() }) {
                            Text(text = "refresh")
                        }

                        if (rss != null)
                            RssList(this@MviMainActivity, rss)
                    }
                }
            }
        }
    }
}

