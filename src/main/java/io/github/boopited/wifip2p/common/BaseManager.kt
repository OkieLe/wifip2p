package io.github.boopited.wifip2p.common

import android.content.Context
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import androidx.annotation.CallSuper
import io.github.boopited.wifip2p.WifiP2pStateMonitor
import java.net.InetAddress

abstract class BaseManager(protected val context: Context): WifiP2pStateMonitor.StateCallback {

    private val stateMonitor = WifiP2pStateMonitor.get(context)

    protected var localDevice: WifiP2pDevice? = null
    protected var p2pConnection: WifiP2pInfo? = null
    protected var p2pDeviceList: WifiP2pDeviceList? = null
    protected var p2pGroup: WifiP2pGroup? = null
    protected var p2pNetwork: NetworkInfo? = null

    init {
        check(WifiUtils.checkWifiSupport(context))
    }

    @CallSuper
    open fun start() {
        stateMonitor.addCallback(this)
        stateMonitor.start()
    }

    @CallSuper
    open fun stop() {
        stateMonitor.stop()
        stateMonitor.removeCallback(this)
    }

    fun isGroupOwner(): Boolean {
        return p2pConnection?.isGroupOwner ?: false
    }

    fun isGroupFormed(): Boolean {
        return p2pConnection?.groupFormed ?: false
    }

    fun groupOwnerAddress(): InetAddress? {
        return p2pConnection?.groupOwnerAddress
    }

    fun getPeersList(): List<WifiP2pDevice> {
        return p2pDeviceList?.deviceList.orEmpty().toList()
    }

    fun getGroupInfo(): WifiP2pGroup? {
        localDevice?.isServiceDiscoveryCapable
        return p2pGroup
    }

    override fun onWifiP2pState(enable: Boolean) {
    }

    override fun onDiscoveryStateChanged(start: Boolean) {
    }

    override fun onConnectionChanged(
        p2pInfo: WifiP2pInfo?,
        networkInfo: NetworkInfo?,
        groupInfo: WifiP2pGroup?
    ) {
        p2pConnection = p2pInfo
        p2pNetwork = networkInfo
        p2pGroup = groupInfo
    }

    override fun onDeviceInfoChanged(device: WifiP2pDevice?) {
        localDevice = device
    }

    override fun onPeersChanged(deviceList: WifiP2pDeviceList?) {
        p2pDeviceList = deviceList
    }
}