package com.example.omdb.ui.fragment.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb.data.local.datastore.setting.SettingDataStore
import com.example.omdb.data.local.datastore.setting.Theme
import com.example.omdb.domain.MovieUseCase
import com.example.omdb.utils.helper.ConnectionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelSetting @Inject constructor(
    private val useCase: MovieUseCase,
    private val settingDataStore: SettingDataStore,
) : ViewModel() {
    val connectionState = useCase.connectionState
    val settingPreferences get() = settingDataStore.preferences

    var lastConnectionState: ConnectionHelper.ConnectionState? = null

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            settingDataStore.updateTheme(theme)
        }
    }

    fun isNetworkAvailable(): Boolean {
        return useCase.isNetworkAvailable()
    }

    fun wifiSetting() {
        useCase.wifiSetting()
    }

    fun cellularSetting() {
        useCase.cellularSetting()
    }

    fun resetConnectionState() {
        useCase.resetConnectionState()
    }
}
