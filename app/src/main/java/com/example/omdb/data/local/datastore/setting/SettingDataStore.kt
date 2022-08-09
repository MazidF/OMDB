package com.example.omdb.data.local.datastore.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.omdb.di.qualifier.DispatcherIO
import com.example.omdb.utils.saveTheme
import com.example.omdb.utils.sharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DATA_STORE_NAME = "ThemeDataStore"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

@Singleton
class SettingDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
) {
    private val dataStore = context.dataStore

    val preferences = dataStore.data.map { preference ->
        val theme = Theme.get(preference[SettingPreferencesKey.KEY_THEME] ?: Theme.AUTO.name)

        SettingPreferencesInfo(
            theme = theme,
        )
    }.catch { cause ->

    }.flowOn(dispatcher)

    suspend fun updateTheme(theme: Theme) {
        dataStore.edit {
            it[SettingPreferencesKey.KEY_THEME] = theme.name
        }
        context.sharedPreferences().saveTheme(theme)
    }

    private object SettingPreferencesKey {
        val KEY_THEME = stringPreferencesKey("keyTheme")
    }
}
