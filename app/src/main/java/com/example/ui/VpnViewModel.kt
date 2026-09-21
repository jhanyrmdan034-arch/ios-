package com.example.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PreferencesManager
import com.example.data.ServerRepository
import com.example.model.AppLanguage
import com.example.model.PingResult
import com.example.model.ServerLocation
import com.example.model.TrafficStats
import com.example.model.VpnProtocol
import com.example.model.VpnState
import com.example.network.PingManager
import com.example.vpn.NovaVpnService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class VpnViewModel(
    private val preferencesManager: PreferencesManager,
    private val serverRepository: ServerRepository = ServerRepository()
) : ViewModel() {

    val language: StateFlow<AppLanguage> = preferencesManager.language
    val hasSelectedLanguageOnStart: StateFlow<Boolean> = preferencesManager.hasSelectedLanguageOnStart
    val servers: StateFlow<List<ServerLocation>> = serverRepository.servers
    val selectedProtocol: StateFlow<VpnProtocol> = preferencesManager.selectedProtocol
    val killSwitchEnabled: StateFlow<Boolean> = preferencesManager.killSwitchEnabled

    private val _vpnState = MutableStateFlow(VpnState.DISCONNECTED)
    val vpnState: StateFlow<VpnState> = _vpnState.asStateFlow()

    private val _currentServer = MutableStateFlow(
        serverRepository.getServerById(preferencesManager.selectedServerId.value)
    )
    val currentServer: StateFlow<ServerLocation> = _currentServer.asStateFlow()

    private val _isSmartMode = MutableStateFlow(
        preferencesManager.selectedServerId.value == serverRepository.smartAutoServer.id
    )
    val isSmartMode: StateFlow<Boolean> = _isSmartMode.asStateFlow()

    private val _trafficStats = MutableStateFlow(TrafficStats())
    val trafficStats: StateFlow<TrafficStats> = _trafficStats.asStateFlow()

    // 20 recent speed samples for the live neon wave graph
    private val _speedHistory = MutableStateFlow<List<Float>>(List(20) { 0f })
    val speedHistory: StateFlow<List<Float>> = _speedHistory.asStateFlow()

    private val _isTestingPings = MutableStateFlow(false)
    val isTestingPings: StateFlow<Boolean> = _isTestingPings.asStateFlow()

    private val _pingResults = MutableStateFlow<Map<String, PingResult>>(emptyMap())
    val pingResults: StateFlow<Map<String, PingResult>> = _pingResults.asStateFlow()

    private var trafficJob: Job? = null
    private var timerJob: Job? = null

    init {
        // Initial ping check in background
        viewModelScope.launch {
            delay(1000)
            refreshAllPings()
        }
    }

    fun setLanguage(lang: AppLanguage) {
        preferencesManager.setLanguage(lang)
    }

    fun setProtocol(protocol: VpnProtocol) {
        preferencesManager.setSelectedProtocol(protocol)
    }

    fun toggleKillSwitch(enabled: Boolean) {
        preferencesManager.setKillSwitch(enabled)
    }

    fun toggleFavorite(serverId: String) {
        serverRepository.toggleFavorite(serverId)
    }

    fun selectServer(server: ServerLocation) {
        if (server.isSmartAuto) {
            enableSmartMode()
            return
        }
        _isSmartMode.value = false
        _currentServer.value = server
        preferencesManager.setSelectedServerId(server.id)

        // If currently connected, reconnect to new server seamlessly
        if (_vpnState.value == VpnState.CONNECTED) {
            viewModelScope.launch {
                _vpnState.value = VpnState.CONNECTING
                delay(1200)
                _vpnState.value = VpnState.CONNECTED
            }
        }
    }

    fun enableSmartMode() {
        _isSmartMode.value = true
        val bestServer = serverRepository.getBestSmartServer()
        _currentServer.value = serverRepository.smartAutoServer.copy(
            cityEn = "Auto: ${bestServer.countryEn} (${bestServer.flagEmoji})",
            cityFa = "خودکار: ${bestServer.countryFa} (${bestServer.flagEmoji})",
            currentPingMs = bestServer.currentPingMs,
            ip = bestServer.ip
        )
        preferencesManager.setSelectedServerId(serverRepository.smartAutoServer.id)

        if (_vpnState.value == VpnState.CONNECTED) {
            viewModelScope.launch {
                _vpnState.value = VpnState.CONNECTING
                delay(1200)
                _vpnState.value = VpnState.CONNECTED
            }
        }
    }

    fun onConnectClick(activity: Activity, onNeedPrepare: (Intent) -> Unit) {
        when (_vpnState.value) {
            VpnState.DISCONNECTED -> {
                val prepareIntent = VpnService.prepare(activity)
                if (prepareIntent != null) {
                    onNeedPrepare(prepareIntent)
                } else {
                    startVpnConnection(activity)
                }
            }
            VpnState.CONNECTED -> {
                disconnectVpn(activity)
            }
            VpnState.CONNECTING -> {
                disconnectVpn(activity)
            }
            VpnState.DISCONNECTING -> {
                // Ignore while in transition
            }
        }
    }

    fun startVpnConnection(context: Context) {
        viewModelScope.launch {
            _vpnState.value = VpnState.CONNECTING

            // If in smart mode, pick the freshest lowest-ping server
            if (_isSmartMode.value) {
                val best = serverRepository.getBestSmartServer()
                _currentServer.value = serverRepository.smartAutoServer.copy(
                    cityEn = "Auto: ${best.countryEn} (${best.flagEmoji})",
                    cityFa = "خودکار: ${best.countryFa} (${best.flagEmoji})",
                    currentPingMs = best.currentPingMs,
                    ip = best.ip
                )
            }

            // Simulate realistic cryptographic handshake & tunnel establishment
            delay(1500)

            // Start Android VpnService
            val serverName = _currentServer.value.let {
                "${it.flagEmoji} ${it.localizedCountry(language.value)}"
            }
            NovaVpnService.start(context, serverName)

            _vpnState.value = VpnState.CONNECTED
            startTrafficSimulation()
        }
    }

    fun disconnectVpn(context: Context) {
        viewModelScope.launch {
            _vpnState.value = VpnState.DISCONNECTING
            delay(800)
            NovaVpnService.stop(context)
            _vpnState.value = VpnState.DISCONNECTED
            stopTrafficSimulation()
        }
    }

    private fun startTrafficSimulation() {
        stopTrafficSimulation()

        var duration = 0L
        var totalDown = 0f
        var totalUp = 0f

        timerJob = viewModelScope.launch {
            while (_vpnState.value == VpnState.CONNECTED) {
                delay(1000)
                duration++
                _trafficStats.value = _trafficStats.value.copy(
                    sessionDurationSeconds = duration
                )
            }
        }

        trafficJob = viewModelScope.launch {
            while (_vpnState.value == VpnState.CONNECTED) {
                delay(600)
                // Realistic fluctuations based on connection quality
                val baseSpeed = when {
                    _currentServer.value.currentPingMs < 60 -> 65f
                    _currentServer.value.currentPingMs < 120 -> 42f
                    else -> 25f
                }
                val jitter = Random.nextFloat() * 18f - 9f
                val downloadSpeed = (baseSpeed + jitter).coerceIn(8.5f, 120.0f)
                val uploadSpeed = (downloadSpeed * 0.35f + (Random.nextFloat() * 4f)).coerceIn(3.0f, 45.0f)

                totalDown += (downloadSpeed * 0.6f / 8f) // MB per 0.6s
                totalUp += (uploadSpeed * 0.6f / 8f)

                _trafficStats.value = _trafficStats.value.copy(
                    downloadSpeedMbps = String.format("%.1f", downloadSpeed).toFloat(),
                    uploadSpeedMbps = String.format("%.1f", uploadSpeed).toFloat(),
                    totalDownloadedMb = String.format("%.1f", totalDown).toFloat(),
                    totalUploadedMb = String.format("%.1f", totalUp).toFloat(),
                    pingMs = _currentServer.value.currentPingMs
                )

                // Update live wave graph history
                val updated = _speedHistory.value.drop(1) + downloadSpeed
                _speedHistory.value = updated
            }
        }
    }

    private fun stopTrafficSimulation() {
        trafficJob?.cancel()
        timerJob?.cancel()
        trafficJob = null
        timerJob = null
        _trafficStats.value = TrafficStats()
        _speedHistory.value = List(20) { 0f }
    }

    fun refreshAllPings() {
        if (_isTestingPings.value) return
        viewModelScope.launch {
            _isTestingPings.value = true
            val currentServers = servers.value

            PingManager.pingAllServers(currentServers) { result ->
                _pingResults.value = _pingResults.value + (result.serverId to result)
                if (!result.isTesting) {
                    serverRepository.updatePing(result.serverId, result.latencyMs)
                    if (_currentServer.value.id == result.serverId) {
                        _currentServer.value = _currentServer.value.copy(currentPingMs = result.latencyMs)
                    }
                }
            }

            _isTestingPings.value = false
        }
    }

    fun testSingleServerPing(server: ServerLocation) {
        viewModelScope.launch {
            _pingResults.value = _pingResults.value + (server.id to PingResult(server.id, server.currentPingMs, isTesting = true))
            val result = PingManager.pingServer(server)
            _pingResults.value = _pingResults.value + (server.id to result)
            serverRepository.updatePing(server.id, result.latencyMs)
            if (_currentServer.value.id == server.id) {
                _currentServer.value = _currentServer.value.copy(currentPingMs = result.latencyMs)
            }
        }
    }
}
