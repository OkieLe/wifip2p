package io.github.boopited.wifip2p

import android.content.Context
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Looper
import android.util.Log
import io.github.boopited.wifip2p.common.BaseManager
import kotlin.properties.Delegates

class GroupOwnerManager(context: Context, private val callback: Callback): BaseManager(context) {

    private var manager: WifiP2pManager by Delegates.notNull()
    private var channel: WifiP2pManager.Channel by Delegates.notNull()

    interface Callback {
        fun onChannelDisconnected()
        fun onPeersList(peers: WifiP2pDeviceList)
    }

    override fun start() {
        super.start()
    }

    override fun stop() {
        super.stop()
    }

    fun init() {
        manager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(context, Looper.getMainLooper()) {
            callback.onChannelDisconnected()
        }
    }

    /**
     * discover available peer list
     */
    fun discoverPeers() {
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
    fun requestPeers() {
        manager.requestPeers(channel) { peers ->
            //请求对等节点列表操作成功
            Log.d(TAG, "$peers")
        }
    }

    /**
     * stop peers discovery
     */
    fun stopPeerDiscovery() {
        manager.stopPeerDiscovery(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d(TAG, "stop discover Peers success")
            }

            override fun onFailure(reason: Int) {
                Log.d(TAG, "stop discover Peers fail: $reason")
            }
        })
    }

    /**
     * connect by MAC address(hardware address)
     */
    fun connect(deviceAddress: String) {
        val config = WifiP2pConfig()
        config.deviceAddress = deviceAddress
        config.wps.setup = WpsInfo.PBC
        manager.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                println("connect device success")
            }

            override fun onFailure(reason: Int) {
                println("connect device fail: $reason")
            }
        })
    }

    /**
     * invoke this method to connect a p2p device
     */
    fun connect(device: WifiP2pDevice) {
        connect(device.deviceAddress)
    }

    fun cancelConnect() {
        manager.cancelConnect(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                println("connect cancel success")
            }

            override fun onFailure(reason: Int) {
                println("connect cancel fail: $reason")
            }
        })
    }

    /**
     * request the group info
     */
    fun requestGroup() {
        manager.requestGroupInfo(channel) { group ->
            val groupPassword = group.passphrase
        }
    }

    /**
     * create a group
     */
    fun createGroup() {
        manager.createGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onFailure(reason: Int) {
                println("create group fail: $reason")

            }

            override fun onSuccess() {
                println("create group success")
            }
        })
    }

    /**
     * remove a group
     */
    fun removeGroup() {
        manager.removeGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                println("remove group success")
            }

            override fun onFailure(reason: Int) {
                println("remove group success: $reason")
            }

        })
    }

    companion object {
        private const val TAG = "GroupOwnerManager"
    }
}