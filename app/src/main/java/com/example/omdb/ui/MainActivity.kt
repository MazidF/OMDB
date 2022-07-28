package com.example.omdb.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.omdb.R
import com.example.omdb.data.local.datastore.setting.Theme
import com.example.omdb.databinding.ActivityMainBinding
import com.example.omdb.ui.fragment.setting.ViewModelSetting
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val navController by lazy {
        findNavController(R.id.container)
    }

    private val viewModelSetting: ViewModelSetting by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        NavigationUI.setupWithNavController(bottom, navController)
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModelSetting.settingPreferences.collectLatest { info ->
                setupTheme(info.theme)
            }
        }
    }

    private fun setupTheme(theme: Theme) {
        if (AppCompatDelegate.getDefaultNightMode() != theme.mode) {
            AppCompatDelegate.setDefaultNightMode(theme.mode)
        }
    }
}