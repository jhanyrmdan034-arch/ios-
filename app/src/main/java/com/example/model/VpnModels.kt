package com.example.model

enum class VpnState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    DISCONNECTING
}

enum class VpnProtocol(val displayName: String, val descriptionEn: String, val descriptionFa: String) {
    WIREGUARD("WireGuard", "Ultra fast, modern, low battery consumption", "فوق‌العاده سریع، نسل جدید با مصرف باتری کم"),
    OPENVPN_UDP("OpenVPN (UDP)", "Optimized for gaming and streaming without buffering", "بهینه‌سازی شده برای گیمینگ و استریم بدون لگ"),
    OPENVPN_TCP("OpenVPN (TCP)", "Maximum stability and bypasses restricted firewalls", "حداکثر پایداری و عبور از فایروال‌های سخت"),
    V2RAY("V2Ray / VMess", "Advanced stealth anti-filtering technology", "پروتکل ضد فیلتر پیشرفته با پنهان‌سازی ترافیک"),
    IKEV2("IKEv2 / IPsec", "Fast reconnection when switching WiFi and cellular", "اتصال مجدد فوق سریع هنگام جابجایی اینترنت")
}

data class TrafficStats(
    val downloadSpeedMbps: Float = 0f,
    val uploadSpeedMbps: Float = 0f,
    val totalDownloadedMb: Float = 0f,
    val totalUploadedMb: Float = 0f,
    val sessionDurationSeconds: Long = 0L,
    val pingMs: Int = 0
)

data class PingResult(
    val serverId: String,
    val latencyMs: Int,
    val isTesting: Boolean = false,
    val isSuccess: Boolean = true
)
