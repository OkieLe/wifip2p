package io.github.boopited.wifip2p

import android.content.Context
import android.net.NetworkInfo
import android.net.wifi.p2p.*
import android.os.Looper
import io.github.boopited.wifip2p.common.BaseManager
import io.github.boopited.wifip2p.p2p.*
import kotlin.properties.Delegates

class GroupClientManager(context: Context, private val callback: Callback): BaseManager(context) {

    private var manager: WifiP2pManager by Delegates.notNull()
    private var channel: WifiP2pManager.Channel by Delegates.notNull()

    interface Callback {
        fun onChannelDisconnected()
        fun onPeersDiscovered(success: Boolean)
        fun onPeersList(peers: List<WifiP2pDevice>)
        fun onGroupInfo(group: WifiP2pGroup?)
        fun onP2pEnabled(enable: Boolean)
    }

    init {
        manager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(context, Looper.getMainLooper()) {
            callback.onChannelDisconnected()
        }
    }

    override fun start() {
        super.start()
        manager.discoverPeers(channel, {
            callback.onPeersDiscovered(true)
        }, {
            callback.onPeersDiscovered(false)
        })
    }

    fun refreshPeers() {
        manager.queryPeers(channel) { peers: WifiP2pDeviceList? ->
            p2pDeviceList = peers
            callback.onPeersList(getPeersList())
        }
    }

    fun refreshGroupInfo() {
        manager.queryGroupInfo(channel) { group: WifiP2pGroup? ->
            p2pGroup = group
            callback.onGroupInfo(p2pGroup)
        }
    }

    override fun stop() {
        manager.stopPeerDiscovery(channel)
        super.stop()
    }

    override fun onWifiP2pState(enable: Boolean) {
        super.onWifiP2pState(enable)
        callback.onP2pEnabled(enable)
    }

    override fun onPeersChanged(deviceList: WifiP2pDeviceList?) {
        super.onPeersChanged(deviceList)
        callback.onPeersList(getPeersList())
    }

    override fun onConnectionChanged(
        p2pInfo: WifiP2pInfo?,
        networkInfo: NetworkInfo?,
        groupInfo: WifiP2pGroup?
    ) {
        super.onConnectionChanged(p2pInfo, networkInfo, groupInfo)
        callback.onGroupInfo(groupInfo)
    }
}