package com.example.data

import com.example.model.ServerLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ServerRepository {

    val nationalNetServerIds = listOf("nat_de", "nat_nl", "nat_us")

    private val initialServers = listOf(
        ServerLocation(
            id = "nat_de",
            countryEn = "Germany (National Net)",
            countryFa = "سرور آلمان مخصوص نت ملی",
            cityEn = "Fastest Server",
            cityFa = "سرور پرسرعت و پایدار",
            flagEmoji = "🇩🇪",
            host = "fra.speedtest.net",
            ip = "185.220.101.5",
            defaultPingMs = 36,
            currentPingMs = 36,
            loadPercentage = 26,
            isRecommended = true,
            tags = listOf("مخصوص نت ملی", "TLS Masking", "پیشنهادی")
        ),
        ServerLocation(
            id = "nat_nl",
            countryEn = "Netherlands (National Net)",
            countryFa = "سرور هلند مخصوص نت ملی",
            cityEn = "Clean IP",
            cityFa = "آی‌پی تمیز و اختصاصی",
            flagEmoji = "🇳🇱",
            host = "ams.speedtest.net",
            ip = "194.126.177.10",
            defaultPingMs = 42,
            currentPingMs = 42,
            loadPercentage = 30,
            isRecommended = true,
            tags = listOf("مخصوص نت ملی", "Clean IP", "پایدار")
        ),
        ServerLocation(
            id = "nat_us",
            countryEn = "USA (National Net)",
            countryFa = "سرور آمریکا مخصوص نت ملی",
            cityEn = "Direct Route",
            cityFa = "تونل مستقیم بدون افت",
            flagEmoji = "🇺🇸",
            host = "nyc.speedtest.net",
            ip = "104.244.76.13",
            defaultPingMs = 105,
            currentPingMs = 105,
            loadPercentage = 40,
            isRecommended = true,
            tags = listOf("مخصوص نت ملی", "نامحدود", "پرسرعت")
        ),
        ServerLocation(
            id = "de_fra",
            countryEn = "Germany",
            countryFa = "آلمان",
            cityEn = "High Speed",
            cityFa = "پرسرعت",
            flagEmoji = "🇩🇪",
            host = "fra.speedtest.net",
            ip = "185.220.101.5",
            defaultPingMs = 38,
            currentPingMs = 38,
            loadPercentage = 42,
            isRecommended = true,
            tags = listOf("Fastest", "P2P", "10 Gbps")
        ),
        ServerLocation(
            id = "us_nyc",
            countryEn = "United States",
            countryFa = "ایالات متحده",
            cityEn = "Unlimited",
            cityFa = "نامحدود",
            flagEmoji = "🇺🇸",
            host = "nyc.speedtest.net",
            ip = "104.244.76.13",
            defaultPingMs = 112,
            currentPingMs = 112,
            loadPercentage = 68,
            isRecommended = true,
            tags = listOf("Streaming", "Netflix", "Hulu")
        ),
        ServerLocation(
            id = "nl_ams",
            countryEn = "Netherlands",
            countryFa = "هلند",
            cityEn = "Low Latency",
            cityFa = "تاخیر کم",
            flagEmoji = "🇳🇱",
            host = "ams.speedtest.net",
            ip = "194.126.177.10",
            defaultPingMs = 45,
            currentPingMs = 45,
            loadPercentage = 31,
            isRecommended = true,
            tags = listOf("Maximum Privacy", "P2P", "No Logs")
        ),
        ServerLocation(
            id = "fi_hel",
            countryEn = "Finland",
            countryFa = "فنلاند",
            cityEn = "Zero Logs",
            cityFa = "بدون لاگ",
            flagEmoji = "🇫🇮",
            host = "hel.speedtest.net",
            ip = "95.216.14.82",
            defaultPingMs = 52,
            currentPingMs = 52,
            loadPercentage = 24,
            isRecommended = true,
            tags = listOf("Nordic Shield", "Zero Logs", "Ultra Low Load")
        ),
        ServerLocation(
            id = "tr_ist",
            countryEn = "Turkey",
            countryFa = "ترکیه",
            cityEn = "Lowest Ping",
            cityFa = "کمترین پینگ",
            flagEmoji = "🇹🇷",
            host = "ist.speedtest.net",
            ip = "185.118.140.22",
            defaultPingMs = 28,
            currentPingMs = 28,
            loadPercentage = 55,
            isRecommended = true,
            tags = listOf("Lowest Ping", "Gaming", "Middle East Gateway")
        ),
        ServerLocation(
            id = "gb_lon",
            countryEn = "United Kingdom",
            countryFa = "انگلستان",
            cityEn = "High Speed",
            cityFa = "پرسرعت",
            flagEmoji = "🇬🇧",
            host = "lon.speedtest.net",
            ip = "82.165.197.1",
            defaultPingMs = 49,
            currentPingMs = 49,
            loadPercentage = 52,
            tags = listOf("BBC iPlayer", "Streaming")
        ),
        ServerLocation(
            id = "ca_tor",
            countryEn = "Canada",
            countryFa = "کانادا",
            cityEn = "Privacy",
            cityFa = "حریم خصوصی",
            flagEmoji = "🇨🇦",
            host = "tor.speedtest.net",
            ip = "192.99.148.10",
            defaultPingMs = 120,
            currentPingMs = 120,
            loadPercentage = 38,
            tags = listOf("Privacy", "Fast")
        ),
        ServerLocation(
            id = "jp_tyo",
            countryEn = "Japan",
            countryFa = "ژاپن",
            cityEn = "Gaming",
            cityFa = "گیمینگ",
            flagEmoji = "🇯🇵",
            host = "tyo.speedtest.net",
            ip = "133.242.18.5",
            defaultPingMs = 180,
            currentPingMs = 180,
            loadPercentage = 44,
            tags = listOf("Anime", "Gaming Asia")
        )
    )

    val smartAutoServer = ServerLocation(
        id = "smart_auto",
        countryEn = "Smart Location",
        countryFa = "سرور هوشمند خودکار",
        cityEn = "Automatic Best",
        cityFa = "انتخاب خودکار بهترین سرور",
        flagEmoji = "⚡",
        host = "1.1.1.1",
        ip = "Dynamic Auto",
        defaultPingMs = 28,
        currentPingMs = 28,
        loadPercentage = 18,
        isSmartAuto = true,
        isRecommended = true,
        tags = listOf("AI Auto Selection", "Lowest Ping", "Zero Lag")
    )

    private val _servers = MutableStateFlow(initialServers)
    val servers: StateFlow<List<ServerLocation>> = _servers.asStateFlow()

    fun updatePing(serverId: String, pingMs: Int) {
        _servers.value = _servers.value.map {
            if (it.id == serverId) it.copy(currentPingMs = pingMs) else it
        }
    }

    fun toggleFavorite(serverId: String) {
        _servers.value = _servers.value.map {
            if (it.id == serverId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun getBestSmartServer(): ServerLocation {
        // Smart algorithm: pick server with lowest ping + load score
        val candidate = _servers.value.minByOrNull { it.currentPingMs + (it.loadPercentage / 2) }
        return candidate ?: _servers.value.first()
    }

    fun getServerById(id: String): ServerLocation {
        if (id == smartAutoServer.id) return smartAutoServer
        return _servers.value.find { it.id == id } ?: smartAutoServer
    }
}
