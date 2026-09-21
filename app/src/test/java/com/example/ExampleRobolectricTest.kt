package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ServerRepository
import com.example.localization.Strings
import com.example.model.AppLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Nova VPN", appName)
  }

  @Test
  fun `verify requested country servers exist`() {
    val repo = ServerRepository()
    val servers = repo.servers.value

    val countriesEn = servers.map { it.countryEn }
    assertTrue(countriesEn.contains("Germany"))
    assertTrue(countriesEn.contains("United States"))
    assertTrue(countriesEn.contains("Netherlands"))
    assertTrue(countriesEn.contains("Finland"))
    assertTrue(countriesEn.contains("Turkey"))
  }

  @Test
  fun `verify smart auto server selection works`() {
    val repo = ServerRepository()
    val best = repo.getBestSmartServer()
    assertNotNull(best)
    assertTrue(best.currentPingMs > 0)
  }

  @Test
  fun `verify bilingual strings for Persian and English`() {
    val titleEn = Strings.appTitle(AppLanguage.ENGLISH)
    val titleFa = Strings.appTitle(AppLanguage.PERSIAN)
    assertEquals("Nova VPN", titleEn)
    assertEquals("نوا وی‌پی‌ان", titleFa)

    val connectEn = Strings.tapToConnect(AppLanguage.ENGLISH)
    val connectFa = Strings.tapToConnect(AppLanguage.PERSIAN)
    assertEquals("TAP TO CONNECT", connectEn)
    assertEquals("برای اتصال ضربه بزنید", connectFa)
  }

  @Test
  fun `verify navigation tabs and national tunnel strings`() {
    assertEquals("خانه", Strings.tabHome(AppLanguage.PERSIAN))
    assertEquals("پشتیبانی", Strings.tabSupport(AppLanguage.PERSIAN))
    assertEquals("کانال تلگرام ما", Strings.telegramChannelBtn(AppLanguage.PERSIAN))
    assertEquals("سرورها", Strings.tabServers(AppLanguage.PERSIAN))
    assertEquals("تانل نت ملی", Strings.tabNationalTunnel(AppLanguage.PERSIAN))

    assertEquals("Home", Strings.tabHome(AppLanguage.ENGLISH))
    assertEquals("Support", Strings.tabSupport(AppLanguage.ENGLISH))
    assertEquals("Our Telegram Channel", Strings.telegramChannelBtn(AppLanguage.ENGLISH))
    assertEquals("Servers", Strings.tabServers(AppLanguage.ENGLISH))
    assertEquals("National Tunnel", Strings.tabNationalTunnel(AppLanguage.ENGLISH))

    assertEquals("دور زدن محدودیت‌های نت ملی و فیلترینگ شدید", Strings.nationalTunnelTitle(AppLanguage.PERSIAN))
    assertEquals("سرور آلمان مخصوص نت ملی", Strings.nationalServerGermany(AppLanguage.PERSIAN))
    assertEquals("سرور هلند مخصوص نت ملی", Strings.nationalServerNetherlands(AppLanguage.PERSIAN))
    assertEquals("سرور آمریکا مخصوص نت ملی", Strings.nationalServerUsa(AppLanguage.PERSIAN))

    assertEquals("اشتراک‌گذاری با دیگران", Strings.shareProposalTitle(AppLanguage.PERSIAN))
    assertEquals("Share VPN with Others", Strings.shareProposalTitle(AppLanguage.ENGLISH))
    assertEquals("اشتراک‌گذاری وی‌پی‌ان", Strings.shareVpnButton(AppLanguage.PERSIAN))
    assertEquals("Share VPN", Strings.shareVpnButton(AppLanguage.ENGLISH))
  }
}

