package com.example.vpn

import android.net.VpnService
import android.os.Bundle
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.vpn.databinding.ActivityMainBinding
import de.blinkt.openvpn.core.ProfileManager
import de.blinkt.openvpn.core.VpnStatus        // если уже не импортирован
import de.blinkt.openvpn.core.OpenVPNService  // понадобится в disconnect()
import de.blinkt.openvpn.core.VpnProfile


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val pm: ProfileManager by lazy { ProfileManager.getInstance(this) }

    private val vpnPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) connect()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.btnConnect.setOnClickListener {
            if (VpnStatus.isVPNActive()) disconnect() else prepareAndConnect()
        }
    }

    /* ---------- helpers ---------- */

    private fun prepareAndConnect() {
        VpnService.prepare(this)?.let { vpnPermission.launch(it) } ?: connect()
    }

    private fun connect() {
        val profile = ensureProfile()
        Log.d("FASTVPN", "Connecting with profile ${profile.name}")
        pm.connect(this, profile)                  // <-- главное отличие
        binding.btnConnect.text = "DISCONNECT"
    }

    private fun disconnect() {
        Log.d("FASTVPN", "Disconnect broadcast")
        sendBroadcast(Intent(OpenVPNService.DISCONNECT_VPN))
        binding.btnConnect.text = "CONNECT"
    }

    private fun ensureProfile(): VpnProfile {
        pm.loadProfiles(this)                      // подстраховка

        if (pm.profiles.isEmpty()) {
            val cfg = assets.open("default.ovpn").bufferedReader().readText()
            pm.addProfile(cfg, "Default", true)    // true = сделать активным
        }
        return pm.profiles.first()
    }
}
