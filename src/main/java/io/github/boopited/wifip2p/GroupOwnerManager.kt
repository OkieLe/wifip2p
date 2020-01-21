package io.github.boopited.wifip2p

import android.content.Context
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Looper
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

    companion object {
        private const val TAG = "GroupOwnerManager"
    }
}