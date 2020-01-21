package io.github.boopited.wifip2p.p2p

import android.net.wifi.p2p.WifiP2pManager
import android.util.Log

private const val TAG = "P2pGroup"

/**
 * request the group info
 */
fun requestGroup(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.requestGroupInfo(channel) { group ->
        val groupPassword = group.passphrase
    }
}

/**
 * create a group
 */
fun createGroup(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.createGroup(channel, object : WifiP2pManager.ActionListener {
        override fun onFailure(reason: Int) {
            Log.d(TAG, "create group fail: $reason")

        }

        override fun onSuccess() {
            Log.d(TAG, "create group success")
        }
    })
}

/**
 * remove a group
 */
fun removeGroup(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.removeGroup(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "remove group success")
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "remove group success: $reason")
        }

    })
}