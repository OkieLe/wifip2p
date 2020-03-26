package io.github.boopited.wifip2p.service

import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo
import android.os.Looper
import io.github.boopited.wifip2p.WifiP2pStateMonitor
import io.github.boopited.wifip2p.common.WifiUtils
import kotlin.properties.Delegates

class P2pServiceFinder(
    private val context: Context,
    private val callback: Callback,
    serviceType: Int = WifiP2pServiceInfo.SERVICE_TYPE_ALL
) {

    private val stateMonitor = WifiP2pStateMonitor.get(context)

    private var manager: WifiP2pManager by Delegates.notNull()
    private var channel: WifiP2pManager.Channel by Delegates.notNull()

    private var wifiP2pEnabled = false
    private val serviceRequest by lazy { createServiceRequest(serviceType) }

    interface Callback {
        fun onP2pEnabled(enable: Boolean)
        fun onP2pError()
        fun onDeviceBusy()
        fun onServiceFound(name: String, registrationType: String, device: WifiP2pDevice)
        fun onServiceInfo(fullName: String, records: Map<String, String>, device: WifiP2pDevice)
    }

    private val stateCallback = object : WifiP2pStateMonitor.StateCallback {
        override fun onWifiP2pState(enable: Boolean) {
            wifiP2pEnabled = enable
            callback.onP2pEnabled(enable)
        }

        override fun onDiscoveryStateChanged(start: Boolean) {}
        override fun onConnectionChanged(connected: Boolean) {}
        override fun onDeviceInfoChanged(device: WifiP2pDevice?) {}
        override fun onPeersChanged() {}
    }

    init {
        check(WifiUtils.checkWifiSupport(context))
        stateMonitor.addCallback(stateCallback)
        stateMonitor.start()
        manager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(context, Looper.getMainLooper()) {}
    }

    fun startDiscovery() {
        if (wifiP2pEnabled) {
            addListener()
            addRequest()
            discovery()
        }
    }

    private fun addListener() {
        manager.listenDnsSdResponse(
            channel,
            { instanceName, registrationType, srcDevice ->
                callback.onServiceFound(instanceName, registrationType, srcDevice)
            },
            { fullDomainName, txtRecordMap, srcDevice ->
                callback.onServiceInfo(fullDomainName, txtRecordMap, srcDevice)
            }
        )
    }

    private fun addRequest() {
        manager.addServiceRequest(channel, serviceRequest, {}, {
            handleError(it)
        })
    }

    private fun removeRequest() {
        manager.removeServiceRequest(channel, serviceRequest)
    }

    private fun discovery() {
        manager.discoverServices(channel, {}, {
            handleError(it)
        })
    }

    private fun handleError(reason: Int) {
        when (reason) {
            WifiP2pManager.P2P_UNSUPPORTED,
            WifiP2pManager.ERROR -> {
                callback.onP2pError()
            }
            WifiP2pManager.BUSY -> {
                callback.onDeviceBusy()
            }
        }
        removeRequest()
    }

    fun shutdown() {
        manager.clearServiceRequests(channel)
        stateMonitor.stop()
        stateMonitor.removeCallback(stateCallback)
    }
}