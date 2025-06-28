class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val pm by lazy { ProfileManager.getInstance(this) }

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
