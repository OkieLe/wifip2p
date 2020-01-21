package io.github.boopited.wifip2p.p2p

import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest

private const val TAG = "P2pService"

fun addService(manager: WifiP2pManager, channel: WifiP2pManager.Channel, service: WifiP2pServiceInfo) {
    manager.addLocalService(channel, service, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
        }

        override fun onFailure(reason: Int) {
        }
    })
}

fun removeService(manager: WifiP2pManager, channel: WifiP2pManager.Channel, service: WifiP2pServiceInfo) {
    manager.removeLocalService(channel, service, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
        }

        override fun onFailure(reason: Int) {
        }
    })
}

fun clearServices(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.clearLocalServices(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
        }

        override fun onFailure(reason: Int) {
        }
    })
}

fun discoverServices(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.discoverServices(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
        }

        override fun onFailure(reason: Int) {
        }
    })
}

fun addServiceRequest(manager: WifiP2pManager, channel: WifiP2pManager.Channel, request: WifiP2pServiceRequest) {
    manager.addServiceRequest(channel, request, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
        }

        override fun onFailure(reason: Int) {
        }
    })
}

fun removeServiceRequest(manager: WifiP2pManager, channel: WifiP2pManager.Channel, request: WifiP2pServiceRequest) {
    manager.removeServiceRequest(channel, request, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
        }

        override fun onFailure(reason: Int) {
        }
    })
}

fun clearServiceRequests(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.clearServiceRequests(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
        }

        override fun onFailure(reason: Int) {
        }
    })
}

fun listenDnsSdResponse(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.setDnsSdResponseListeners(channel,
        { instanceName, registrationType, srcDevice ->

        },
        { fullDomainName, txtRecordMap, srcDevice ->

        })
}

fun listenUpnpResponse(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
    manager.setUpnpServiceResponseListener(channel) { uniqueServiceNames, srcDevice ->

    }
}