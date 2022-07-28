package com.example.omdb.utils.helper

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val manager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val _connectionState = MutableLiveData(ConnectionState.NONE)
    val connectionState: LiveData<ConnectionState> = _connectionState

    init {
        observeConnectionState()
        updateState()
    }

    private fun observeConnectionState() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _connectionState.postValue(ConnectionState.CONNECTED)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _connectionState.postValue(ConnectionState.DISCONNECTED)
            }
        }

        manager.requestNetwork(request, callback)
    }

    private fun updateState() {
        _connectionState.postValue(getState())
    }

    private fun getState(): ConnectionState {
        return if (this.isNetworkAvailable()) {
            ConnectionState.CONNECTED
        } else {
            ConnectionState.DISCONNECTED
        }
    }

    fun isNetworkAvailable(): Boolean {
        return manager.activeNetworkInfo?.isConnected == true
    }

    fun goToWifiSettings() {
        context.startActivity(
            Intent(Settings.ACTION_WIFI_SETTINGS).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    fun goToDataRoamingSettings() {
        context.startActivity(
            Intent(Settings.ACTION_DATA_ROAMING_SETTINGS).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    fun resetConnectionState() {
        _connectionState.postValue(ConnectionState.NONE)
    }

    enum class ConnectionState {
        CONNECTED,
        DISCONNECTED,
        NONE;
    }
}