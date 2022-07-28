package com.example.omdb.data.local.datastore.setting

import androidx.appcompat.app.AppCompatDelegate

enum class Theme(
    val mode: Int,
) {
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    NIGHT(AppCompatDelegate.MODE_NIGHT_YES),
    AUTO(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    companion object {
        fun get(theme: String): Theme {
            return valueOf(theme)
        }
    }
}