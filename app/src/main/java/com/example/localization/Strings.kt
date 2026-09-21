package com.example.localization

import com.example.model.AppLanguage

object Strings {
    fun appTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "نوا وی‌پی‌ان" else "Nova VPN"
    fun appSubtitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "امنیت نامحدود • اتصال فوق سریع" else "Ultra Secure • Blazing Speed"

    // Language
    fun selectLanguageTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "انتخاب زبان برنامه" else "Select App Language"
    fun selectLanguageDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "زبان مورد نظر خود را برای ادامه انتخاب کنید" else "Choose your preferred language to continue"
    fun english(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "انگلیسی (English)" else "English"
    fun persian(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "فارسی" else "Persian (فارسی)"
    fun continueBtn(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تایید و ورود" else "Continue"
    fun switchLanguage(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تغییر زبان" else "Change Language"

    // Connection States
    fun stateDisconnected(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "قطع شده" else "DISCONNECTED"
    fun stateConnecting(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "در حال اتصال..." else "CONNECTING..."
    fun stateConnected(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "متصل شدید" else "PROTECTED & CONNECTED"
    fun stateDisconnecting(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "در حال قطع اتصال..." else "DISCONNECTING..."

    fun tapToConnect(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "برای اتصال ضربه بزنید" else "TAP TO CONNECT"
    fun tapToDisconnect(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "قطع اتصال" else "DISCONNECT"
    fun connectingTunnel(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "در حال ایمن‌سازی تونل شبکه..." else "Securing encrypted tunnel..."

    // Smart Server
    fun smartConnectTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سرور هوشمند خودکار" else "Smart Auto Connect"
    fun smartConnectDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "انتخاب خودکار کمترین پینگ و بهترین سرعت" else "Auto-picks the fastest server with lowest ping"
    fun smartActiveBadge(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "حالت هوشمند فعال" else "SMART ACTIVE"

    // Server Selection
    fun selectLocation(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "انتخاب لوکیشن سرور" else "Select Server Location"
    fun changeLocation(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تغییر سرور" else "Change"
    fun currentServer(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سرور فعال فعلی" else "Current Server"
    fun searchServers(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "جستجوی کشور یا شهر..." else "Search country or city..."
    fun allServers(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "همه سرورها" else "All Servers"
    fun recommendedServers(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "پیشنهادی" else "Recommended"
    fun favoriteServers(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "علاقه‌مندی‌ها" else "Favorites"
    fun serverLoad(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "بار سرور:" else "Server Load:"

    // Ping Testing
    fun pingTest(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تست پینگ زنده" else "Live Ping Test"
    fun testAllPings(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تست پینگ همه سرورها" else "Test All Pings"
    fun pinging(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "در حال پینگ..." else "Testing Ping..."
    fun pingMs(lang: AppLanguage, ms: Int) = if (lang == AppLanguage.PERSIAN) "$ms میلی‌ثانیه" else "$ms ms"
    fun pingExcellent(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "عالی" else "Excellent"
    fun pingGood(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "خوب" else "Good"
    fun pingMedium(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "متوسط" else "Fair"
    fun pingHigh(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "بالا" else "High"
    fun pingSummary(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "بررسی تاخیر واقعی شبکه به سرورهای مقصد" else "Real-time socket latency to remote edge servers"

    // Traffic & Stats
    fun download(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "دانلود" else "DOWNLOAD"
    fun upload(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "آپلود" else "UPLOAD"
    fun sessionDuration(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "مدت اتصال" else "Duration"
    fun totalTransferred(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "حجم مصرفی" else "Data Used"
    fun virtualIp(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "آی‌پی مجازی شما" else "Virtual IP"
    fun realIpProtected(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "آی‌پی اصلی شما پنهان شد" else "Your real IP is hidden"

    // IP Before & After Connection and Security Status
    fun ipBeforeConnection(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "آی‌پی قبل از اتصال (واقعی)" else "IP Before Connect (Real)"
    fun ipAfterConnection(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "آی‌پی بعد از اتصال (امن)" else "IP After Connect (Secure)"
    fun ipLocationIran(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "ایران 🇮🇷 • همراه اول / مخابرات" else "Iran 🇮🇷 • Domestic ISP"
    fun ipExposedWarning(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "آشکار ⚠️" else "Exposed ⚠️"
    fun ipMaskedSecure(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "مخفی شده 🔒" else "Masked 🔒"
    fun connectedSecureTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "متصل شده و ایمن هست" else "Connected & Secure"
    fun connectedSecureDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "ترافیک شما رمزنگاری شده و هویت آنلاین شما کاملاً محفوظ است." else "Traffic is 256-bit encrypted and your identity is protected."
    fun disconnectedTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "عدم اتصال (محافظت نشده)" else "Not Connected (Unprotected)"
    fun disconnectedDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "برای تغییر آی‌پی و ایمن‌سازی ترافیک، دکمه اتصال را بزنید." else "Tap connect to mask your IP and encrypt all network traffic."
    fun connectingStatusTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "در حال برقراری اتصال ایمن..." else "Establishing Secure Tunnel..."
    fun connectingStatusDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "در حال تغییر آی‌پی و رمزنگاری ترافیک شبکه..." else "Changing IP address and encrypting packets..."
    fun waitingForConnection(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "در انتظار اتصال..." else "Waiting for connect..."

    // Security & Features
    fun securityFeatures(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تنظیمات امنیتی و پروتکل" else "Security & Protocol"
    fun protocol(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "پروتکل اتصال" else "Protocol"
    fun killSwitch(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "کیل سوییچ اضطراری (Kill Switch)" else "Kill Switch"
    fun killSwitchDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "قطع کامل اینترنت در صورت قطع ناگهانی وی‌پی‌ان" else "Block internet if VPN connection unexpectedly drops"
    fun encryption(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "رمزنگاری نظامی" else "Military Encryption"
    fun encryptionType(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "ChaCha20-Poly1305 (۲۵۶ بیتی)" else "ChaCha20-Poly1305 (256-bit)"
    fun dnsProtection(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "محافظت در برابر نشت DNS" else "DNS Leak Shield"
    fun protectedStatus(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "فعال و ایمن" else "Active & Protected"

    // Navigation Tabs
    fun tabHome(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "خانه" else "Home"
    fun tabSupport(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "پشتیبانی" else "Support"
    fun tabServers(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سرورها" else "Servers"
    fun tabNationalTunnel(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تانل نت ملی" else "National Tunnel"

    // Support Section
    fun supportTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "مرکز پشتیبانی و ارتباط با ما" else "Support & Contact Center"
    fun supportSubtitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "پشتیبانی ۲۴ ساعته و کانال رسمی اطلاع‌رسانی سرورها" else "24/7 Support & Official Server Channel"
    fun telegramChannelBtn(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "کانال تلگرام ما" else "Our Telegram Channel"
    fun telegramChannelDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "دریافت آخرین اخبار سرورها، کانفیگ‌های جدید و اطلاع‌رسانی اختلالات" else "Latest server updates, fresh configs and alerts"
    fun directSupportBtn(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "پشتیبانی" else "Support"
    fun directSupportDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "ارتباط مستقیم با کارشناسان فنی و پاسخگویی به سوالات شما" else "Direct chat with technical team for queries & assistance"
    fun openTelegram(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "ورود به تلگرام" else "Open Telegram"
    fun supportFaqTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "راهنمای سریع" else "Quick FAQ"
    fun supportFaqDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "در صورت وجود اختلال در اتصال، ابتدا سرور دیگری را انتخاب کنید یا حالت نت ملی را فعال نمایید." else "If experiencing connection issues, try another server or switch to National Tunnel."

    // Share VPN Suggestion
    fun shareProposalTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "اشتراک‌گذاری با دیگران" else "Share VPN with Others"
    fun shareProposalQuestion(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "اتصال شما برقرار و ایمن است! آیا می‌خواهید این وی‌پی‌ان را با دوستان و دیگران به اشتراک بگذارید تا آن‌ها هم دسترسی آزاد داشته باشند؟" else "Your connection is live and secure! Would you like to share this VPN with friends so they can stay connected too?"
    fun shareVpnButton(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "اشتراک‌گذاری وی‌پی‌ان" else "Share VPN"
    fun shareLaterButton(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "شاید بعداً" else "Maybe Later"
    fun shareMessageContent(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN)
        "سلام! من از این وی‌پی‌ان پرسرعت و پایدار استفاده می‌کنم و اتصال عالیه، پیشنهاد می‌کنم تو هم برای دور زدن محدودیت‌ها نصب کنی:\nhttps://t.me/novavpn_official"
    else
        "Hey! I am using this fast, secure VPN and it connects smoothly. Check it out to bypass restrictions:\nhttps://t.me/novavpn_official"
    fun shareChooserTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "اشتراک‌گذاری وی‌پی‌ان از طریق..." else "Share VPN via..."

    // National Net Tunnel (دور زدن محدودیت‌های نت ملی و فیلترینگ شدید)
    fun nationalTunnelTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "دور زدن محدودیت‌های نت ملی و فیلترینگ شدید" else "Bypass National Firewall & Severe Filtering"
    fun nationalTunnelSubtitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سرورهای تونل‌شده و اختصاصی برای دسترسی آزاد به اینترنت جهانی" else "Dedicated obfuscated servers to bypass extreme national censorship"
    fun nationalTunnelActive(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "اتصال نت ملی فعال است" else "National Bypass Active"
    fun nationalTunnelInactive(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "اتصال نت ملی غیرفعال است" else "National Bypass Inactive"

    fun nationalServersTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سرورهای اتصال مخصوص نت ملی" else "Dedicated National Net Bypass Servers"
    fun nationalServerGermany(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سرور آلمان مخصوص نت ملی" else "Germany (National Net Bypass)"
    fun nationalServerNetherlands(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سرور هلند مخصوص نت ملی" else "Netherlands (National Net Bypass)"
    fun nationalServerUsa(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سرور آمریکا مخصوص نت ملی" else "USA (National Net Bypass)"

    fun nationalServerGermanyDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تونل مستقیم پرسرعت ضد فیلترینگ" else "Ultra high speed anti-censorship tunnel"
    fun nationalServerNetherlandsDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "نود اختصاصی بدون اختلال و پینگ پایین" else "Low-latency stable domestic relay"
    fun nationalServerUsaDesc(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "استتار کامل ترافیک و پهنای باند نامحدود" else "Fully obfuscated unlimited tunnel"

    fun testPingAction(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تست پینگ" else "Test Ping"
    fun testAllPingsAction(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تست پینگ همه سرورها" else "Test All Pings"
    fun connectAction(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "اتصال" else "Connect"
    fun disconnectAction(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "قطع اتصال" else "Disconnect"
    fun serverConnectedStatus(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "متصل" else "Connected"
    fun serverStandbyStatus(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "آماده اتصال" else "Ready"

    // Countries
    fun germany(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "آلمان" else "Germany"
    fun usa(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "ایالات متحده" else "United States"
    fun netherlands(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "هلند" else "Netherlands"
    fun finland(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "فنلاند" else "Finland"
    fun turkey(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "ترکیه" else "Turkey"
    fun unitedKingdom(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "انگلستان" else "United Kingdom"
    fun canada(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "کانادا" else "Canada"
    fun japan(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "ژاپن" else "Japan"

    // Notification
    fun vpnConnectedNotification(lang: AppLanguage, serverName: String) =
        if (lang == AppLanguage.PERSIAN) "متصل به $serverName • اینترنت شما ایمن است"
        else "Connected to $serverName • Your traffic is encrypted"
}
