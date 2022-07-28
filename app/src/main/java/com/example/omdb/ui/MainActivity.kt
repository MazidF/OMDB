package com.example.omdb.ui

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.omdb.R
import com.example.omdb.data.local.datastore.setting.Theme
import com.example.omdb.databinding.ActivityMainBinding
import com.example.omdb.databinding.ConnectionDialogBinding
import com.example.omdb.ui.fragment.setting.ViewModelSetting
import com.example.omdb.utils.helper.ConnectionHelper
import com.example.omdb.utils.hideBackground
import com.example.omdb.utils.launch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val navController by lazy {
        findNavController(R.id.container)
    }

    private val viewModelSetting: ViewModelSetting by viewModels()
    private var connectionDialog: AlertDialog? = null


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
        launch {
            viewModelSetting.settingPreferences.collectLatest { info ->
                setupTheme(info.theme)
            }
        }
        launch {
            viewModelSetting.connectionState.observe(this@MainActivity) { state ->
                when (state) {
                    ConnectionHelper.ConnectionState.CONNECTED -> {
                        updateConnectionState(true)
                    }
                    ConnectionHelper.ConnectionState.DISCONNECTED -> {
                        updateConnectionState(false)
                        showConnectionDialog()
                    }
                }
                if (state != ConnectionHelper.ConnectionState.NONE) {
                    viewModelSetting.resetConnectionState()
                }
            }
        }
        navController.addOnDestinationChangedListener(this)
    }

    private fun updateConnectionState(isConnected: Boolean) {
        if (isConnected) {
            connectionDialog?.cancel()
            connectionDialog = null
        }
    }

    private fun showConnectionDialog() {
        val binding = ConnectionDialogBinding.inflate(layoutInflater).apply {
            wifi.setOnClickListener {
                viewModelSetting.wifiSetting()
            }
            cellular.setOnClickListener {
                viewModelSetting.cellularSetting()
            }
            offline.setOnClickListener {
                connectionDialog?.cancel()
            }
        }

        connectionDialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .create()
            .hideBackground()
            .also {
                it.show()
                it.window?.setLayout(1000, WRAP_CONTENT)
            }
    }

    private fun setupTheme(theme: Theme) {
        if (AppCompatDelegate.getDefaultNightMode() != theme.mode) {
            AppCompatDelegate.setDefaultNightMode(theme.mode)
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ): Unit = with(binding) {
        when(destination.id) {
            R.id.fragmentHome, R.id.fragmentSetting -> {
                bottom.isVisible = true
            }
            else -> {
                bottom.isVisible = false
            }
        }
    }
}