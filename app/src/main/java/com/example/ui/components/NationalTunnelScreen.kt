package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.Strings
import com.example.model.AppLanguage
import com.example.model.PingResult
import com.example.model.ServerLocation
import com.example.model.VpnState
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun NationalTunnelScreen(
    vpnState: VpnState,
    currentServer: ServerLocation,
    servers: List<ServerLocation>,
    language: AppLanguage,
    isTestingPings: Boolean,
    pingResults: Map<String, PingResult>,
    onSelectAndConnectServer: (ServerLocation) -> Unit,
    onDisconnect: () -> Unit,
    onTestPing: (ServerLocation) -> Unit,
    onTestAllPings: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Filter dedicated national net servers (Germany, Netherlands, USA)
    val nationalServers = servers.filter { it.id in listOf("nat_de", "nat_nl", "nat_us") }.ifEmpty {
        // Fallback to finding Germany, Netherlands, USA if not prefixed
        servers.filter { it.id in listOf("de_fra", "nl_ams", "us_nyc") }
    }

    val isNationalConnected = vpnState == VpnState.CONNECTED &&
            (currentServer.id in listOf("nat_de", "nat_nl", "nat_us") ||
                    currentServer.id in listOf("de_fra", "nl_ams", "us_nyc"))

    val rotationTransition = rememberInfiniteTransition(label = "national_spin")
    val spinAngle by rotationTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(850, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Header Banner with title: "دور زدن محدودیت‌های نت ملی و فیلترینگ شدید"
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CyberSurface),
            border = BorderStroke(1.2.dp, Brush.horizontalGradient(listOf(NeonCyan, NeonPurple))),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("national_tunnel_header_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.linearGradient(listOf(NeonCyan, NeonPurple)))
                        ) {
                            Icon(
                                imageVector = Icons.Default.VpnLock,
                                contentDescription = "National Bypass",
                                tint = Color.Black,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = Strings.nationalTunnelTitle(language),
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = Strings.nationalTunnelSubtitle(language),
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Status Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isNationalConnected) NeonEmerald.copy(alpha = 0.2f) else CyberSurfaceVariant)
                            .border(1.dp, if (isNationalConnected) NeonEmerald else CyberCardBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (isNationalConnected) {
                                Strings.serverConnectedStatus(language)
                            } else {
                                Strings.serverStandbyStatus(language)
                            },
                            color = if (isNationalConnected) NeonEmerald else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Test All Pings Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CyberDarkBg)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.PERSIAN) "۳ سرور تونل‌شده مخصوص شرایط فیلترینگ شدید" else "3 Dedicated National Bypass Servers",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = onTestAllPings,
                        enabled = !isTestingPings,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberSurfaceVariant,
                            contentColor = NeonCyan
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("test_all_national_pings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Test All",
                            modifier = Modifier
                                .size(14.dp)
                                .rotate(if (isTestingPings) spinAngle else 0f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isTestingPings) Strings.pinging(language) else Strings.testAllPingsAction(language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Section Title: سرورهای اتصال مخصوص نت ملی
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Text(
                text = Strings.nationalServersTitle(language),
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (language == AppLanguage.PERSIAN) "آلمان • هلند • آمریکا" else "Germany • Netherlands • USA",
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Dedicated National Servers Cards
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            nationalServers.forEach { server ->
                val isCurrentConnected = vpnState == VpnState.CONNECTED && currentServer.id == server.id
                val pingInfo = pingResults[server.id]
                val isTesting = pingInfo?.isTesting == true
                val displayPing = pingInfo?.latencyMs ?: server.currentPingMs

                val serverTitle = when (server.id) {
                    "nat_de", "de_fra" -> Strings.nationalServerGermany(language)
                    "nat_nl", "nl_ams" -> Strings.nationalServerNetherlands(language)
                    "nat_us", "us_nyc" -> Strings.nationalServerUsa(language)
                    else -> server.localizedCountry(language)
                }

                val serverDesc = when (server.id) {
                    "nat_de", "de_fra" -> Strings.nationalServerGermanyDesc(language)
                    "nat_nl", "nl_ams" -> Strings.nationalServerNetherlandsDesc(language)
                    "nat_us", "us_nyc" -> Strings.nationalServerUsaDesc(language)
                    else -> server.ip
                }

                NationalServerCard(
                    server = server,
                    serverTitle = serverTitle,
                    serverDesc = serverDesc,
                    displayPing = displayPing,
                    isTestingPing = isTesting,
                    isCurrentConnected = isCurrentConnected,
                    language = language,
                    onConnect = { onSelectAndConnectServer(server) },
                    onDisconnect = onDisconnect,
                    onTestPing = { onTestPing(server) }
                )
            }
        }
    }
}

@Composable
private fun NationalServerCard(
    server: ServerLocation,
    serverTitle: String,
    serverDesc: String,
    displayPing: Int,
    isTestingPing: Boolean,
    isCurrentConnected: Boolean,
    language: AppLanguage,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onTestPing: () -> Unit
) {
    val rotationTransition = rememberInfiniteTransition(label = "server_card_spin")
    val spinAngle by rotationTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(850, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentConnected) CyberSurfaceVariant else CyberSurface
        ),
        border = BorderStroke(
            1.2.dp,
            if (isCurrentConnected) NeonEmerald else CyberCardBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("national_server_card_${server.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Row 1: Flag, Title, City, Ping Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = server.flagEmoji,
                        fontSize = 32.sp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = serverTitle,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (isCurrentConnected) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NeonEmerald.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = Strings.serverConnectedStatus(language),
                                        color = NeonEmerald,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = serverDesc,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Ping Indicator Box
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberDarkBg)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        displayPing < 50 -> NeonEmerald
                                        displayPing < 115 -> NeonCyan
                                        else -> NeonOrange
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "$displayPing ms",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Row 2: Action Buttons (تست پینگ & اتصال / قطع اتصال)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Test Ping Button
                OutlinedButton(
                    onClick = onTestPing,
                    enabled = !isTestingPing,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CyberCardBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = NeonCyan
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("test_ping_${server.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Test Ping",
                        tint = NeonCyan,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(if (isTestingPing) spinAngle else 0f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isTestingPing) Strings.pinging(language) else Strings.testPingAction(language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Connect / Disconnect Button
                Button(
                    onClick = if (isCurrentConnected) onDisconnect else onConnect,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrentConnected) NeonRed else NeonCyan,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(44.dp)
                        .testTag("connect_btn_${server.id}")
                ) {
                    Icon(
                        imageVector = if (isCurrentConnected) Icons.Default.PowerSettingsNew else Icons.Default.VpnKey,
                        contentDescription = "Connect",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isCurrentConnected) Strings.disconnectAction(language) else Strings.connectAction(language),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
