package io.github.boopited.wifip2p

import android.content.Context
import android.net.wifi.p2p.*
import io.github.boopited.wifip2p.common.BaseManager
import io.github.boopited.wifip2p.p2p.*

class GroupClientManager(context: Context, private val callback: Callback): BaseManager(context) {

    interface Callback {
        fun onP2pEnabled(enable: Boolean)
        fun onPeersDiscovered(success: Boolean)
        fun onConnectResult(success: Boolean)
        fun onGroupFormed()
        fun onPeersList(peers: List<WifiP2pDevice>)
        fun onGroupInfo(group: WifiP2pGroup?)
    }

    override fun start() {
        super.start()
        if (isWifiP2pEnabled()) {
            manager.discoverPeers(channel, {
                callback.onPeersDiscovered(true)
            }, {
                callback.onPeersDiscovered(false)
            })
        } else {
            throw IllegalStateException("Enable wifi p2p first")
        }
    }

    fun refreshPeers() {
        manager.queryPeers(channel) { peers: WifiP2pDeviceList? ->
            p2pDeviceList = peers
            callback.onPeersList(getPeersList())
        }
    }

    fun connect(mac: String) {
        manager.connect(channel, mac, {
            callback.onConnectResult(true)
        }, {
            callback.onConnectResult(false)
        })
    }

    fun cancelConnect() {
        manager.cancelConnect(channel)
    }

    override fun stop() {
        cancelConnect()
        manager.stopPeerDiscovery(channel)
        super.stop()
    }

    override fun onWifiP2pEnabled(enable: Boolean) {
        callback.onP2pEnabled(enable)
    }

    override fun onPeersList(peers: WifiP2pDeviceList?) {
        callback.onPeersList(getPeersList())
    }

    override fun onConnectionInfo(info: WifiP2pInfo?) {
        super.onConnectionInfo(info)
        if (info?.groupFormed == true) {
            callback.onGroupFormed()
        }
    }

    override fun onGroupInfo(info: WifiP2pGroup?) {
        super.onGroupInfo(info)
        callback.onGroupInfo(getGroupInfo())
    }
}