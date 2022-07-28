package com.example.omdb.ui.fragment.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.omdb.R
import com.example.omdb.data.local.datastore.setting.Theme
import com.example.omdb.databinding.FragmentSettingBinding
import com.example.omdb.utils.launch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSetting : Fragment(R.layout.fragment_setting) {
    private var _binding: FragmentSettingBinding? = null
    private val binding: FragmentSettingBinding get() = _binding!!

    private val viewModel: ViewModelSetting by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        var lastIndex = 0
        autoSwitch.setOnClickListener {
            if (autoSwitch.isChecked) {
                viewModel.updateTheme(Theme.AUTO)
            } else {
                themeOption.select(lastIndex)
            }
        }
        themeOption.setONSelectionChangeListener { index ->
            val theme = when (index) {
                0 -> Theme.LIGHT
                1 -> Theme.NIGHT
                else -> return@setONSelectionChangeListener
            }
            lastIndex = index
            viewModel.updateTheme(theme)
        }
    }

    private fun observe() = with(binding) {
        launch {
            viewModel.settingPreferences.collect { info ->
                setupTheme(info.theme)
            }
        }
    }

    private fun setupTheme(theme: Theme) = with(binding) {
        val optionIndex = when (theme) {
            Theme.LIGHT -> 0
            Theme.NIGHT -> 1
            Theme.AUTO -> -1
        }
        themeOption.select(optionIndex)
        autoSwitch.isChecked = optionIndex == -1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}