package io.github.boopited.wifip2p.p2p

import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log

private const val TAG = "P2pConnection"

/**
 * connect by MAC address(hardware address)
 */
fun connect(manager: WifiP2pManager, channel: WifiP2pManager.Channel, deviceAddress: String) {
    val config = WifiP2pConfig()
    config.deviceAddress = deviceAddress
    config.wps.setup = WpsInfo.PBC
    manager.connect(channel, config, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "connect device success")
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "connect device fail: $reason")
        }
    })
}

/**
 * invoke this method to connect a p2p device
 */
fun connect(manager: WifiP2pManager, channel: WifiP2pManager.Channel, device: WifiP2pDevice) {
    connect(manager, channel, device.deviceAddress)
}

fun cancelConnect(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.cancelConnect(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "connect cancel success")
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "connect cancel fail: $reason")
        }
    })
}

fun requestConnectionInfo(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.requestConnectionInfo(channel) { p2pinfo ->

    }
}