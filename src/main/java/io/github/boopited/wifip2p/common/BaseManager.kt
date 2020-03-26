package io.github.boopited.wifip2p.common

import android.content.Context
import android.net.wifi.p2p.*
import android.os.Looper
import androidx.annotation.CallSuper
import io.github.boopited.wifip2p.WifiP2pStateMonitor
import io.github.boopited.wifip2p.p2p.queryConnectionInfo
import io.github.boopited.wifip2p.p2p.queryGroupInfo
import io.github.boopited.wifip2p.p2p.queryPeers
import java.net.InetAddress
import kotlin.properties.Delegates

abstract class BaseManager(protected val context: Context) {

    private val stateMonitor = WifiP2pStateMonitor.get(context)

    protected var manager: WifiP2pManager by Delegates.notNull()
    protected var channel: WifiP2pManager.Channel by Delegates.notNull()

    private var wifiP2pEnabled = false

    protected var localDevice: WifiP2pDevice? = null
    protected var p2pConnection: WifiP2pInfo? = null
    protected var p2pGroup: WifiP2pGroup? = null
    protected var p2pDeviceList: WifiP2pDeviceList? = null

    init {
        check(WifiUtils.checkWifiSupport(context))
        manager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(context, Looper.getMainLooper()) {
            onChannelDisconnected()
        }
    }

    private val stateCallback = object : WifiP2pStateMonitor.StateCallback {
        override fun onWifiP2pState(enable: Boolean) {
            wifiP2pEnabled = enable
            onWifiP2pEnabled(enable)
        }

        override fun onDiscoveryStateChanged(start: Boolean) {
        }

        override fun onConnectionChanged(connected: Boolean) {
            if (connected) {
                manager.queryConnectionInfo(channel) { info ->
                    p2pConnection = info
                    onConnectionInfo(p2pConnection)
                }
                manager.queryGroupInfo(channel) { group ->
                    p2pGroup = group
                    onGroupInfo(p2pGroup)
                }
            }
        }

        override fun onDeviceInfoChanged(device: WifiP2pDevice?) {
            localDevice = device
        }

        override fun onPeersChanged() {
            manager.queryPeers(channel) { deviceList ->
                p2pDeviceList = deviceList
                onPeersList(p2pDeviceList)
            }
        }
    }

    @CallSuper
    open fun start() {
        stateMonitor.addCallback(stateCallback)
        stateMonitor.start()
    }

    @CallSuper
    open fun stop() {
        stateMonitor.stop()
        stateMonitor.removeCallback(stateCallback)
    }

    fun isWifiP2pEnabled(): Boolean {
        return wifiP2pEnabled
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

    fun getGroupInfo(): WifiP2pGroup? {
        return p2pGroup
    }

    fun getPeersList(): List<WifiP2pDevice> {
        return p2pDeviceList?.deviceList.orEmpty().toList()
    }

    open fun onChannelDisconnected() {}
    open fun onWifiP2pEnabled(enable: Boolean) {}
    open fun onConnectionInfo(info: WifiP2pInfo?) {}
    open fun onGroupInfo(info: WifiP2pGroup?) {}
    open fun onPeersList(peers: WifiP2pDeviceList?) {}
}