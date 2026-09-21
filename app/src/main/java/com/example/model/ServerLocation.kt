package com.example.model

data class ServerLocation(
    val id: String,
    val countryEn: String,
    val countryFa: String,
    val cityEn: String,
    val cityFa: String,
    val flagEmoji: String,
    val host: String,
    val ip: String,
    val defaultPingMs: Int,
    val currentPingMs: Int = defaultPingMs,
    val loadPercentage: Int = 35,
    val isSmartAuto: Boolean = false,
    val isRecommended: Boolean = false,
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false
) {
    fun localizedCountry(language: AppLanguage): String =
        if (language == AppLanguage.PERSIAN) countryFa else countryEn

    fun localizedCity(language: AppLanguage): String =
        if (language == AppLanguage.PERSIAN) cityFa else cityEn
}
