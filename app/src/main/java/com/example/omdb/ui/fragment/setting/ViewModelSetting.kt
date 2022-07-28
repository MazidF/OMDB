package com.example.omdb.ui.fragment.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb.data.local.datastore.setting.SettingDataStore
import com.example.omdb.data.local.datastore.setting.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelSetting @Inject constructor(
    private val settingDataStore: SettingDataStore,
) : ViewModel() {

    val settingPreferences get() = settingDataStore.preferences

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            settingDataStore.updateTheme(theme)
        }
    }
}
