package com.realpacific.networksensingapp.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.realpacific.networksensingapp.ConnectionStateMonitor
import com.realpacific.networksensingapp.CustomSnackbar

/**
 * Extend this activity whenever you want it to react to network status changes
 */
@SuppressLint("Registered")
open class NetworkSensingBaseActivity : AppCompatActivity(), ConnectionStateMonitor.OnNetworkAvailableCallbacks {

    var snackbar: CustomSnackbar? = null
    private var connectionStateMonitor: ConnectionStateMonitor? = null
    private var viewGroup: ViewGroup? = null

    override fun onResume() {
        super.onResume()
        if (viewGroup == null) viewGroup = findViewById(android.R.id.content)
        if (snackbar == null)
            snackbar = CustomSnackbar.make(viewGroup!!, Snackbar.LENGTH_INDEFINITE).setText("No internet connection.")

        if (connectionStateMonitor == null)
            connectionStateMonitor = ConnectionStateMonitor(this, this)
        //Register
        connectionStateMonitor?.enable()

        // Recheck network status manually whenever activity resumes
        if (connectionStateMonitor?.hasNetworkConnection() == false) onNegative()
        else onPositive()
    }

    override fun onPause() {
        //My whole day effort to handle a memory leak caused by SnackBar, which involved passing WeakReferences & clearing them,
        //making variables null, and so on but later found that
        // it was apparently caused by the support library itself in v28.0.0 and was
        //fixed in androidx 1.1.0+
        snackbar?.dismiss()
        snackbar = null
        //Unregister
        connectionStateMonitor?.disable()
        connectionStateMonitor = null
        super.onPause()
    }

    override fun onPositive() {
        runOnUiThread {
            snackbar?.dismiss()
        }
    }

    override fun onNegative() {
        runOnUiThread {
            snackbar?.setText("No internet connection.")?.show()
        }
    }

}
