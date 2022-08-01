package com.example.omdb.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.omdb.R
import com.example.omdb.data.local.datastore.setting.Theme
import com.example.omdb.databinding.ActivityMainBinding
import com.example.omdb.databinding.ConnectionDialogBinding
import com.example.omdb.ui.fragment.setting.ViewModelSetting
import com.example.omdb.utils.helper.ConnectionHelper.ConnectionState
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
        updateConnectionState(viewModelSetting.isNetworkAvailable())
        setupStatusBar()
    }

    private fun setupStatusBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    private fun observe() {
        launch {
            viewModelSetting.settingPreferences.collectLatest { info ->
                setupTheme(info.theme)
            }
        }
        viewModelSetting.connectionState.observe(this@MainActivity) { state ->
            when (state) {
                ConnectionState.CONNECTED -> {
                    viewModelSetting.lastConnectionState = state
                    updateConnectionState(true)
                }
                ConnectionState.DISCONNECTED -> {
                    updateConnectionState(false)
                }
            }
            if (state != ConnectionState.NONE) {
                viewModelSetting.resetConnectionState()
            }
        }
        navController.addOnDestinationChangedListener(this)
    }

    private fun updateConnectionState(isConnected: Boolean) = with(binding) {
        updateToolbarState(isConnected)

        viewModelSetting.lastConnectionState?.let {
            if (isConnected == it.isConnected()) {
                return@with
            }
        }

        if (isConnected) {
            connectionDialog?.cancel()
            connectionDialog = null
        } else {
            showConnectionDialog()
        }
    }

    private fun updateToolbarState(isConnected: Boolean) = with(binding) {
        val iconResourceId: Int
        val stateResourceId: Int

        if (isConnected) {
            iconResourceId = R.drawable.ic_wifi
            stateResourceId = R.string.online
        } else {
            iconResourceId = R.drawable.ic_no_wifi
            stateResourceId = R.string.offline
        }

        icon.setImageResource(iconResourceId)
        state.text = getString(stateResourceId)
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
                viewModelSetting.lastConnectionState = viewModelSetting.connectionState.value
                connectionDialog?.cancel()
            }
        }

        connectionDialog?.cancel()
        connectionDialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .setCancelable(false)
            .create()
            .hideBackground()
            .also {
                it.show()
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
        when (destination.id) {
            R.id.fragmentHome -> {
                toolbar.isVisible = true
                bottom.isVisible = true
            }
            R.id.fragmentSetting -> {
                toolbar.isVisible = false
                bottom.isVisible = true
            }
            else -> {
                toolbar.isVisible = false
                bottom.isVisible = false
            }
        }
    }
}