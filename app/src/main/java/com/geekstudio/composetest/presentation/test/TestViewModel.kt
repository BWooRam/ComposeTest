package com.geekstudio.composetest.presentation.test


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekstudio.composetest.data.api.RssApi
import com.geekstudio.composetest.data.remote.RssDataSource
import com.geekstudio.composetest.presentation.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.lang.IndexOutOfBoundsException
import java.lang.NullPointerException
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TestViewModel @Inject constructor(
    private val rssDataSource: RssDataSource
) : ViewModel() {
    private val _uiSharedFlow = MutableSharedFlow<BaseUiState>(
        replay = 10,
        extraBufferCapacity = 20,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiSharedFlow = _uiSharedFlow.asSharedFlow()

    fun showRandomErrorUi() {
        val randomSeed = Random.nextInt(0, 3)
        val throwable = when (randomSeed) {
            0 -> Throwable("Throwable Error 발생")
            1 -> NullPointerException("NullPointerException Error 발생")
            else -> IndexOutOfBoundsException("IndexOutOfBoundsException Error 발생")
        }
        Log.d("RssList", "showRandomErrorUi throwable = $throwable")
        _uiSharedFlow.tryEmit(BaseUiState.Error(throwable))
    }

    /**
     *
     */
    fun loadNewsRss() {
        viewModelScope.launch {
            rssDataSource.getNewsRss.execute(RssApi.LanguageType.KR).collect {
                _uiSharedFlow.tryEmit(BaseUiState.Success(it))
            }
        }
    }

}