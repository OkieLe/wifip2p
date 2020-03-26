package io.github.boopited.wifip2p

import android.content.Context
import android.net.NetworkInfo
import android.net.wifi.p2p.*
import android.os.Looper
import io.github.boopited.wifip2p.common.BaseManager
import io.github.boopited.wifip2p.p2p.createConfig
import io.github.boopited.wifip2p.p2p.createGroup
import io.github.boopited.wifip2p.p2p.removeGroup
import kotlin.properties.Delegates

class GroupOwnerManager(
    context: Context, private val callback: Callback,
    groupName: String? = null, groupPassphrase: String? = null
): BaseManager(context) {

    private var manager: WifiP2pManager by Delegates.notNull()
    private var channel: WifiP2pManager.Channel by Delegates.notNull()
    private val config: WifiP2pConfig? =
        if (groupName.isNullOrBlank() || groupPassphrase.isNullOrBlank()) {
            null
        } else {
            createConfig(groupName, groupPassphrase)
        }

    interface Callback {
        fun onChannelDisconnected()
        fun onGroupCreated(success: Boolean)
        fun onGroupInfo(group: WifiP2pGroup?)
        fun onClientsChanged(clients: List<WifiP2pDevice>)
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
        startGroup()
    }

    private fun startGroup() {
        manager.createGroup(channel, config, {
            callback.onGroupCreated(true)
        }, {
            callback.onGroupCreated(false)
        })
    }

    private fun stopGroup() {
        manager.removeGroup(channel)
    }

    override fun stop() {
        stopGroup()
        super.stop()
    }

    override fun onWifiP2pState(enable: Boolean) {
        super.onWifiP2pState(enable)
        callback.onP2pEnabled(enable)
    }

    override fun onConnectionChanged(
        p2pInfo: WifiP2pInfo?,
        networkInfo: NetworkInfo?,
        groupInfo: WifiP2pGroup?
    ) {
        super.onConnectionChanged(p2pInfo, networkInfo, groupInfo)
        callback.onGroupInfo(groupInfo)
        callback.onClientsChanged(groupInfo?.clientList.orEmpty().toList())
    }

    companion object {
        private const val TAG = "GroupOwnerManager"
    }
}