package com.example.vpn

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log

class LoveNestVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == ACTION_DISCONNECT) {
            disconnectVpn()
            return START_NOT_STICKY
        }

        try {
            // Setup genuine Android VpnService Builder
            val builder = Builder()
                .setSession("LoveNest Secure Tunnel")
                .addAddress("10.0.0.2", 24)
                .addRoute("0.0.0.0", 0)
                .addDnsServer("1.1.1.1")
                .addDnsServer("8.8.8.8")
                .setMtu(1500)

            vpnInterface = builder.establish()
            Log.d("LoveNestVPN", "LoveNest VPN Service Established Successfully")

        } catch (e: Exception) {
            Log.e("LoveNestVPN", "Failed to start VPN service", e)
        }

        return START_STICKY
    }

    private fun disconnectVpn() {
        try {
            vpnInterface?.close()
            vpnInterface = null
            stopSelf()
            Log.d("LoveNestVPN", "LoveNest VPN Service Disconnected")
        } catch (e: Exception) {
            Log.e("LoveNestVPN", "Error closing VPN interface", e)
        }
    }

    override fun onDestroy() {
        disconnectVpn()
        super.onDestroy()
    }

    companion object {
        const val ACTION_CONNECT = "com.example.vpn.CONNECT"
        const val ACTION_DISCONNECT = "com.example.vpn.DISCONNECT"
    }
}
