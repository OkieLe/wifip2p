package io.github.boopited.wifip2p.p2p

import android.net.wifi.p2p.WifiP2pManager
import android.util.Log

private const val TAG = "P2pDiscover"

/**
 * discover available peer list
 */
fun discoverPeers(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "discover Peers success")
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "discover Peers fail: $reason")
        }
    })
}

/**
 * request the peer list available
 */
fun requestPeers(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.requestPeers(channel) { peers ->
        //请求对等节点列表操作成功
        Log.d(TAG, "$peers")
    }
}

/**
 * stop peers discovery
 */
fun stopPeerDiscovery(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.stopPeerDiscovery(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "stop discover Peers success")
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "stop discover Peers fail: $reason")
        }
    })
}