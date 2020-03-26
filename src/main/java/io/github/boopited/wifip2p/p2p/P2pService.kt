package io.github.boopited.wifip2p.p2p

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest
import android.util.Log

private const val TAG = "P2pService"

fun WifiP2pManager.addService(
    channel: WifiP2pManager.Channel, service: WifiP2pServiceInfo,
    success: (() -> Unit)? = null, failure: ((Int) -> Unit)? = null
) {
    addLocalService(channel, service, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "add service success")
            success?.invoke()
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "add service fail: $reason")
            failure?.invoke(reason)
        }
    })
}

fun WifiP2pManager.removeService(
    channel: WifiP2pManager.Channel, service: WifiP2pServiceInfo,
    success: (() -> Unit)? = null, failure: ((Int) -> Unit)? = null
) {
    removeLocalService(channel, service, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "remove service success")
            success?.invoke()
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "remove service fail: $reason")
            failure?.invoke(reason)
        }
    })
}

fun WifiP2pManager.clearServices(
    channel: WifiP2pManager.Channel,
    success: (() -> Unit)? = null, failure: ((Int) -> Unit)? = null
) {
    clearLocalServices(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "clear service success")
            success?.invoke()
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "clear service fail: $reason")
            failure?.invoke(reason)
        }
    })
}

fun WifiP2pManager.discoverServices(
    channel: WifiP2pManager.Channel,
    success: (() -> Unit)? = null, failure: ((Int) -> Unit)? = null
) {
    discoverServices(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "discover service success")
            success?.invoke()
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "discover service fail: $reason")
            failure?.invoke(reason)
        }
    })
}

fun WifiP2pManager.addServiceRequest(
    channel: WifiP2pManager.Channel, request: WifiP2pServiceRequest,
    success: (() -> Unit)? = null, failure: ((Int) -> Unit)? = null
) {
    addServiceRequest(channel, request, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "add service request success")
            success?.invoke()
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "add service request fail: $reason")
            failure?.invoke(reason)
        }
    })
}

fun WifiP2pManager.removeServiceRequest(
    channel: WifiP2pManager.Channel, request: WifiP2pServiceRequest,
    success: (() -> Unit)? = null, failure: ((Int) -> Unit)? = null
) {
    removeServiceRequest(channel, request, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "rm service request success")
            success?.invoke()
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "rm service request fail: $reason")
            failure?.invoke(reason)
        }
    })
}

fun WifiP2pManager.clearServiceRequests(
    channel: WifiP2pManager.Channel,
    success: (() -> Unit)? = null, failure: ((Int) -> Unit)? = null
) {
    clearServiceRequests(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d(TAG, "clear service request success")
            success?.invoke()
        }

        override fun onFailure(reason: Int) {
            Log.d(TAG, "clear service request fail: $reason")
            failure?.invoke(reason)
        }
    })
}

fun WifiP2pManager.listenDnsSdResponse(
    channel: WifiP2pManager.Channel,
    serviceListener: ((String, String, WifiP2pDevice) -> Unit)? = null,
    txtRecordListener: ((String, Map<String, String>, WifiP2pDevice) -> Unit)? = null
) {
    setDnsSdResponseListeners(channel, serviceListener, txtRecordListener)
}

fun WifiP2pManager.listenUPnpResponse(
    channel: WifiP2pManager.Channel,
    serviceResponseListener: ((List<String>, WifiP2pDevice) -> Unit)? = null
) {
    setUpnpServiceResponseListener(channel, serviceResponseListener)
}