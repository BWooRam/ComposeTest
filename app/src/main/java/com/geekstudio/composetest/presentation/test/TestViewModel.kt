package com.geekstudio.composetest.presentation.test


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekstudio.composetest.data.api.RssApi
import com.geekstudio.composetest.data.dto.ContactVal
import com.geekstudio.composetest.data.dto.ContactVar
import com.geekstudio.composetest.data.dto.ContactsVal
import com.geekstudio.composetest.data.dto.ContactsVar
import com.geekstudio.composetest.data.remote.RssDataSource
import com.geekstudio.composetest.presentation.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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

    private val randomContactVal = getRandomContactVal()
    private val randomContactVar = getRandomContactVar()

    fun showRandomContactValUi() {
        val tempContactVal = mutableListOf<ContactVal>()
        for (index in 0..10) {
            if (index == 0) {
                tempContactVal.add(index, randomContactVal)
            } else {
                tempContactVal.add(index, getRandomContactVal())
            }
        }
        Log.d("ContactItemCard", "showRandomContactValUi tempContactVal = $tempContactVal")
        _uiSharedFlow.tryEmit(BaseUiState.Success(ContactsVal(tempContactVal)))
    }

    fun showRandomContactVarUi() {
        val tempContactVar = mutableListOf<ContactVar>()
        for (index in 0..10) {
            if (index == 0) {
                tempContactVar.add(index, randomContactVar)
            } else {
                tempContactVar.add(index, getRandomContactVar())
            }
        }
        Log.d("ContactItemCard", "showRandomContactVarUi tempContactVar = $tempContactVar")
        _uiSharedFlow.tryEmit(BaseUiState.Success(ContactsVar(tempContactVar)))
    }

    private fun getRandomContactVal(): ContactVal {
        val random = Random.nextInt(0, 10000)
        val name = "name$random"
        val number = "number$random"
        return ContactVal(
            name = name,
            number = number
        )
    }

    private fun getRandomContactVar(): ContactVar {
        val random = Random.nextInt(10000, 20000)
        val name = "name$random"
        val number = "number$random"
        return ContactVar(
            name = name,
            number = number
        )
    }

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