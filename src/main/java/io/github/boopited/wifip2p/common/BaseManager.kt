package io.github.boopited.wifip2p.common

import android.content.Context
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import androidx.annotation.CallSuper
import io.github.boopited.wifip2p.WifiP2pStateMonitor

abstract class BaseManager(protected val context: Context): WifiP2pStateMonitor.StateCallback {

    private val stateMonitor = WifiP2pStateMonitor.get(context)

    @CallSuper
    open fun start() {
        stateMonitor.addCallback(this)
    }

    @CallSuper
    open fun stop() {
        stateMonitor.removeCallback(this)
    }

    override fun onWifiP2pState(enable: Boolean) {
    }

    override fun onConnectionChanged(
        p2pInfo: WifiP2pInfo?,
        networkInfo: NetworkInfo?,
        groupInfo: WifiP2pGroup?
    ) {
    }

    override fun onDeviceInfoChanged(device: WifiP2pDevice?) {
    }

    override fun onPeersChanged(deviceList: WifiP2pDeviceList?) {
    }

    override fun onDiscoveryStateChanged(start: Boolean) {
    }
}