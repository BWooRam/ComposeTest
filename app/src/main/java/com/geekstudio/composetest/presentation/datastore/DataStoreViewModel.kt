package com.geekstudio.composetest.presentation.datastore


import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekstudio.composetest.TestData
import com.geekstudio.composetest.data.dto.SettingsData
import com.geekstudio.composetest.data.remote.RssDataSource
import com.geekstudio.composetest.data.remote.SettingDataStore
import com.geekstudio.composetest.presentation.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val rssDataSource: RssDataSource,
    @Named("test") private val testDataStore: DataStore<TestData>,
    @Named("setting") private val settingDataStore: DataStore<Preferences>
) : ViewModel() {
    private val _uiSharedFlow = MutableSharedFlow<BaseUiState>(
        replay = 10,
        extraBufferCapacity = 20,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiSharedFlow = _uiSharedFlow.asSharedFlow()

    val isDarkThemeFlow = settingDataStore.data.map { preferences ->
        SettingsData(
            isDarkTheme = preferences[SettingDataStore.PreferencesKey.IS_DARK_THEME]
                ?: false
        )
    }

    val testDataFlow = testDataStore.data

    fun updateTest(isTest: Boolean) {
        viewModelScope.launch {
            testDataStore.updateData { testData ->
                testData.toBuilder()
                    .setIsTest(isTest)
                    .build()
            }
        }
    }

    var currentSettingData: SettingsData? = null
    var currentTestData: TestData? = null

    fun updateDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            settingDataStore.edit { preferences ->
                preferences[SettingDataStore.PreferencesKey.IS_DARK_THEME] = isDarkTheme
            }
        }
    }

}