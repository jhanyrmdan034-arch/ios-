package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AppLanguage
import com.example.model.VpnProtocol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("nova_vpn_prefs", Context.MODE_PRIVATE)

    private val _language = MutableStateFlow(loadLanguage())
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _hasSelectedLanguageOnStart = MutableStateFlow(
        prefs.getBoolean(KEY_HAS_SELECTED_LANG, false)
    )
    val hasSelectedLanguageOnStart: StateFlow<Boolean> = _hasSelectedLanguageOnStart.asStateFlow()

    private val _selectedServerId = MutableStateFlow(
        prefs.getString(KEY_SERVER_ID, "smart_auto") ?: "smart_auto"
    )
    val selectedServerId: StateFlow<String> = _selectedServerId.asStateFlow()

    private val _selectedProtocol = MutableStateFlow(loadProtocol())
    val selectedProtocol: StateFlow<VpnProtocol> = _selectedProtocol.asStateFlow()

    private val _killSwitchEnabled = MutableStateFlow(
        prefs.getBoolean(KEY_KILL_SWITCH, true)
    )
    val killSwitchEnabled: StateFlow<Boolean> = _killSwitchEnabled.asStateFlow()

    fun setLanguage(appLanguage: AppLanguage) {
        prefs.edit()
            .putString(KEY_LANGUAGE, appLanguage.code)
            .putBoolean(KEY_HAS_SELECTED_LANG, true)
            .apply()
        _language.value = appLanguage
        _hasSelectedLanguageOnStart.value = true
    }

    fun setSelectedServerId(id: String) {
        prefs.edit().putString(KEY_SERVER_ID, id).apply()
        _selectedServerId.value = id
    }

    fun setSelectedProtocol(protocol: VpnProtocol) {
        prefs.edit().putString(KEY_PROTOCOL, protocol.name).apply()
        _selectedProtocol.value = protocol
    }

    fun setKillSwitch(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_KILL_SWITCH, enabled).apply()
        _killSwitchEnabled.value = enabled
    }

    private fun loadLanguage(): AppLanguage {
        val code = prefs.getString(KEY_LANGUAGE, null)
        return if (code == "fa") AppLanguage.PERSIAN else AppLanguage.ENGLISH
    }

    private fun loadProtocol(): VpnProtocol {
        val name = prefs.getString(KEY_PROTOCOL, VpnProtocol.WIREGUARD.name)
        return try {
            VpnProtocol.valueOf(name ?: VpnProtocol.WIREGUARD.name)
        } catch (e: Exception) {
            VpnProtocol.WIREGUARD
        }
    }

    companion object {
        private const val KEY_LANGUAGE = "app_lang"
        private const val KEY_HAS_SELECTED_LANG = "has_selected_lang"
        private const val KEY_SERVER_ID = "selected_server_id"
        private const val KEY_PROTOCOL = "selected_protocol"
        private const val KEY_KILL_SWITCH = "kill_switch"
    }
}
