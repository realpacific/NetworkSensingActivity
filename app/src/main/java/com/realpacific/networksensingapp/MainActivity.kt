package com.realpacific.networksensingapp

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Toast
import com.realpacific.networksensingapp.base.NetworkSensingBaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : NetworkSensingBaseActivity() {
    private val wm by lazy {
        applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_switch.isChecked = (wm.wifiState == WifiManager.WIFI_STATE_ENABLED)

        activity_main_switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                wm.isWifiEnabled = true
                wm.reconnect()
                Toast.makeText(this, "Connecting..", Toast.LENGTH_SHORT).show()
            } else {
                wm.isWifiEnabled = false
                Toast.makeText(this, "Turned off.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}