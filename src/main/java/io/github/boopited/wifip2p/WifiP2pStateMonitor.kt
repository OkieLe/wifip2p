package io.github.boopited.wifip2p

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.wifi.p2p.*

class WifiP2pStateMonitor(private val context: Context) {

    interface StateCallback {
        fun onWifiP2pState(enable: Boolean)
        fun onConnectionChanged(p2pInfo: WifiP2pInfo?, networkInfo: NetworkInfo?, groupInfo: WifiP2pGroup?)
        fun onDeviceInfoChanged(device: WifiP2pDevice?)
        fun onPeersChanged(deviceList: WifiP2pDeviceList?)
        fun onDiscoveryStateChanged(start: Boolean)
    }

    private var initialized = false
    private val stateCallbacks = hashSetOf<StateCallback>()

    private val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION)
    }

    fun start() {
        if (!initialized) {
            initialized = true
            context.registerReceiver(stateReceiver, intentFilter)
        }
    }

    fun addCallback(callback: StateCallback) {
        stateCallbacks.add(callback)
    }

    fun removeCallback(callback: StateCallback) {
        stateCallbacks.remove(callback)
    }

    fun stop() {
        if (initialized) {
            context.unregisterReceiver(stateReceiver)
            initialized = false
        }
    }

    private val stateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            when (intent?.action) {
                /**
                 * Wifi p2p enable or disable
                 */
                WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                    val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,
                        WifiP2pManager.WIFI_P2P_STATE_DISABLED)
                    stateCallbacks.forEach {
                        it.onWifiP2pState(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
                    }
                }

                /**
                 * the connection of wifi p2p changed
                 */
                WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                    val p2pInfo = intent.getParcelableExtra<WifiP2pInfo>(WifiP2pManager.EXTRA_WIFI_P2P_INFO)
                    val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiP2pManager.EXTRA_NETWORK_INFO)
                    val groupInfo = intent.getParcelableExtra<WifiP2pGroup>(WifiP2pManager.EXTRA_WIFI_P2P_GROUP)
                    stateCallbacks.forEach {
                        it.onConnectionChanged(p2pInfo, networkInfo, groupInfo)
                    }
                }

                /**
                 * this device details have changed
                 */
                WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                    val device = intent.getParcelableExtra<WifiP2pDevice>(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)
                    stateCallbacks.forEach {
                        it.onDeviceInfoChanged(device)
                    }
                }

                /**
                 * invoke when the list of peers find, register, lost
                 */
                WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                    val devices = intent.getParcelableExtra<WifiP2pDeviceList>(WifiP2pManager.EXTRA_P2P_DEVICE_LIST)
                    stateCallbacks.forEach {
                        it.onPeersChanged(devices)
                    }
                }

                /**
                 * peer discovery has either started or stopped
                 */
                WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION -> {
                    val state = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED)
                    stateCallbacks.forEach {
                        it.onDiscoveryStateChanged(state == WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "WifiP2pStateMonitor"

        @Volatile
        private var INSTANCE: WifiP2pStateMonitor? = null

        fun get(context: Context): WifiP2pStateMonitor {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WifiP2pStateMonitor(context).also { INSTANCE = it }
            }
        }
    }
}