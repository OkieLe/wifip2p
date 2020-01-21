package io.github.boopited.wifip2p

import android.content.Context
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import io.github.boopited.wifip2p.common.BaseManager

class GroupClientManager(context: Context): BaseManager(context) {

    override fun start() {
        super.start()
    }

    override fun stop() {
        super.stop()
    }

    override fun onWifiP2pState(enable: Boolean) {
        super.onWifiP2pState(enable)
    }

    override fun onConnectionChanged(
        p2pInfo: WifiP2pInfo?,
        networkInfo: NetworkInfo?,
        groupInfo: WifiP2pGroup?
    ) {
        super.onConnectionChanged(p2pInfo, networkInfo, groupInfo)
    }
}