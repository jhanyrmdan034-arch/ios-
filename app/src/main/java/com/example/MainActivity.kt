package com.example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.PreferencesManager
import com.example.data.ServerRepository
import com.example.localization.Strings
import com.example.model.AppLanguage
import com.example.model.ServerLocation
import com.example.model.VpnState
import com.example.ui.VpnViewModel
import com.example.ui.components.AppNavTab
import com.example.ui.components.CyberBottomBar
import com.example.ui.components.CyberConnectButton
import com.example.ui.components.LanguageSelectionDialog
import com.example.ui.components.NationalTunnelScreen
import com.example.ui.components.PingTesterDialog
import com.example.ui.components.ServerInfoCard
import com.example.ui.components.ServerSelectionBottomSheet
import com.example.ui.components.ServersScreen
import com.example.ui.components.ShareVpnPromptCard
import com.example.ui.components.TrafficSpeedometerCard
import com.example.ui.screens.SupportScreen
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var serverRepository: ServerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        preferencesManager = PreferencesManager(applicationContext)
        serverRepository = ServerRepository()

        setContent {
            MyApplicationTheme {
                val viewModel: VpnViewModel = viewModel {
                    VpnViewModel(preferencesManager, serverRepository)
                }

                NovaVpnApp(
                    viewModel = viewModel,
                    activity = this@MainActivity
                )
            }
        }
    }
}

@Composable
fun NovaVpnApp(
    viewModel: VpnViewModel,
    activity: Activity
) {
    val context = LocalContext.current

    val language by viewModel.language.collectAsState()
    val hasSelectedLanguageOnStart by viewModel.hasSelectedLanguageOnStart.collectAsState()
    val vpnState by viewModel.vpnState.collectAsState()
    val currentServer by viewModel.currentServer.collectAsState()
    val isSmartMode by viewModel.isSmartMode.collectAsState()
    val trafficStats by viewModel.trafficStats.collectAsState()
    val speedHistory by viewModel.speedHistory.collectAsState()
    val servers by viewModel.servers.collectAsState()
    val isTestingPings by viewModel.isTestingPings.collectAsState()
    val pingResults by viewModel.pingResults.collectAsState()
    val selectedProtocol by viewModel.selectedProtocol.collectAsState()
    val killSwitchEnabled by viewModel.killSwitchEnabled.collectAsState()

    var currentTab by remember { mutableStateOf(AppNavTab.HOME) }
    var showLanguageDialog by remember { mutableStateOf(!hasSelectedLanguageOnStart) }
    var showServerSheet by remember { mutableStateOf(false) }
    var showPingDialog by remember { mutableStateOf(false) }
    var isShareCardDismissed by remember { mutableStateOf(false) }

    LaunchedEffect(vpnState) {
        if (vpnState != VpnState.CONNECTED) {
            isShareCardDismissed = false
        }
    }

    // Launcher for Android VpnService.prepare(activity)
    val vpnPrepareLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.startVpnConnection(context)
        }
    }

    val layoutDirection = if (language == AppLanguage.PERSIAN) {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            containerColor = CyberDarkBg,
            bottomBar = {
                CyberBottomBar(
                    currentTab = currentTab,
                    vpnState = vpnState,
                    language = language,
                    onTabSelected = { currentTab = it }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(CyberDarkBg)
            ) {
                // Background subtle ambient radial glow
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    if (vpnState == VpnState.CONNECTED) NeonCyan.copy(alpha = 0.08f)
                                    else NeonPurple.copy(alpha = 0.04f),
                                    Color.Transparent
                                ),
                                radius = 900f
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    // Top App Bar
                    TopHeaderBar(
                        language = language,
                        isSmartMode = isSmartMode,
                        onOpenLanguage = { showLanguageDialog = true },
                        onOpenPingTester = { showPingDialog = true },
                        onEnableSmart = { viewModel.enableSmartMode() }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    when (currentTab) {
                        AppNavTab.HOME -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // IP Status & Security Banner
                                HeroVisualBanner(
                                    vpnState = vpnState,
                                    currentServer = currentServer,
                                    language = language
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Current Selected Server Info Card
                                ServerInfoCard(
                                    server = currentServer,
                                    isSmartMode = isSmartMode,
                                    language = language,
                                    onClickChange = { currentTab = AppNavTab.SERVERS }
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Center Cyber Glowing Connect Button
                                CyberConnectButton(
                                    vpnState = vpnState,
                                    language = language,
                                    onConnectClick = {
                                        viewModel.onConnectClick(activity) { prepareIntent ->
                                            vpnPrepareLauncher.launch(prepareIntent)
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Live Traffic & Speedometer Card
                                TrafficSpeedometerCard(
                                    vpnState = vpnState,
                                    trafficStats = trafficStats,
                                    speedHistory = speedHistory,
                                    language = language
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Suggestion to share VPN with others after connection
                                AnimatedVisibility(
                                    visible = vpnState == VpnState.CONNECTED && !isShareCardDismissed,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Column {
                                        ShareVpnPromptCard(
                                            language = language,
                                            onDismiss = { isShareCardDismissed = true }
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        AppNavTab.SERVERS -> {
                            ServersScreen(
                                servers = servers,
                                currentServer = currentServer,
                                isSmartMode = isSmartMode,
                                language = language,
                                isTestingPings = isTestingPings,
                                pingResults = pingResults,
                                onSelectServer = { server ->
                                    viewModel.selectServer(server)
                                    currentTab = AppNavTab.HOME
                                },
                                onSelectSmartAuto = {
                                    viewModel.enableSmartMode()
                                    currentTab = AppNavTab.HOME
                                },
                                onTestAllPings = {
                                    viewModel.refreshAllPings()
                                },
                                onTestSinglePing = { server ->
                                    viewModel.testSingleServerPing(server)
                                },
                                onToggleFavorite = { serverId ->
                                    viewModel.toggleFavorite(serverId)
                                }
                            )
                        }

                        AppNavTab.SUPPORT -> {
                            SupportScreen(language = language)
                        }

                        AppNavTab.NATIONAL_TUNNEL -> {
                            NationalTunnelScreen(
                                vpnState = vpnState,
                                currentServer = currentServer,
                                servers = servers,
                                language = language,
                                isTestingPings = isTestingPings,
                                pingResults = pingResults,
                                onSelectAndConnectServer = { server ->
                                    viewModel.selectServer(server)
                                    if (vpnState != VpnState.CONNECTED) {
                                        viewModel.onConnectClick(activity) { prepareIntent ->
                                            vpnPrepareLauncher.launch(prepareIntent)
                                        }
                                    }
                                },
                                onDisconnect = {
                                    if (vpnState == VpnState.CONNECTED) {
                                        viewModel.onConnectClick(activity) {}
                                    }
                                },
                                onTestPing = { server ->
                                    viewModel.testSingleServerPing(server)
                                },
                                onTestAllPings = {
                                    viewModel.refreshAllPings()
                                }
                            )
                        }
                    }
                }
            }

            // Language Selection Dialog (Onboarding or Switcher)
            if (showLanguageDialog) {
                LanguageSelectionDialog(
                    currentLanguage = language,
                    onLanguageSelected = { newLang ->
                        viewModel.setLanguage(newLang)
                        showLanguageDialog = false
                    },
                    onDismiss = {
                        showLanguageDialog = false
                    }
                )
            }

            // Server Selection Bottom Sheet
            if (showServerSheet) {
                ServerSelectionBottomSheet(
                    servers = servers,
                    currentServer = currentServer,
                    isSmartMode = isSmartMode,
                    language = language,
                    isTestingPings = isTestingPings,
                    pingResults = pingResults,
                    onSelectServer = { server ->
                        viewModel.selectServer(server)
                    },
                    onSelectSmartAuto = {
                        viewModel.enableSmartMode()
                    },
                    onTestAllPings = {
                        viewModel.refreshAllPings()
                    },
                    onTestSinglePing = { server ->
                        viewModel.testSingleServerPing(server)
                    },
                    onToggleFavorite = { serverId ->
                        viewModel.toggleFavorite(serverId)
                    },
                    onDismiss = {
                        showServerSheet = false
                    }
                )
            }

            // Dedicated Ping Tester Dialog
            if (showPingDialog) {
                PingTesterDialog(
                    servers = servers,
                    isTesting = isTestingPings,
                    pingResults = pingResults,
                    language = language,
                    onTestAllPings = {
                        viewModel.refreshAllPings()
                    },
                    onDismiss = {
                        showPingDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun TopHeaderBar(
    language: AppLanguage,
    isSmartMode: Boolean,
    onOpenLanguage: () -> Unit,
    onOpenPingTester: () -> Unit,
    onEnableSmart: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        // App Logo & Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CyberSurfaceVariant)
                    .border(1.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Logo",
                    tint = NeonCyan,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = Strings.appTitle(language),
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = Strings.appSubtitle(language),
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        // Action Buttons: Ping Speedometer & Language Selector
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Live Ping Tester Quick Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(CyberSurfaceVariant)
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp))
                    .clickable(onClick = onOpenPingTester)
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Ping Tester",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PING",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Language Switcher Button (Shows flag of current language)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(CyberSurfaceVariant)
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp))
                    .clickable(onClick = onOpenLanguage)
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = language.flag,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (language == AppLanguage.PERSIAN) "فا" else "EN",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun HeroVisualBanner(
    vpnState: VpnState,
    currentServer: ServerLocation,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val isConnected = vpnState == VpnState.CONNECTED
    val isConnecting = vpnState == VpnState.CONNECTING || vpnState == VpnState.DISCONNECTING

    // Real IP before VPN connection
    val realIpBefore = "5.120.88.42"
    // IP after VPN connection seamlessly replaces before-connection IP in the exact same spot
    val activeIp = when {
        isConnected -> currentServer.ip
        isConnecting -> "..."
        else -> realIpBefore
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberSurface),
        border = BorderStroke(
            1.dp,
            if (isConnected) NeonEmerald.copy(alpha = 0.7f)
            else if (isConnecting) NeonCyan.copy(alpha = 0.5f)
            else CyberCardBorder
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("ip_status_security_banner")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // 1. Top Security & Status Row (Compact)
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                if (isConnected) NeonEmerald.copy(alpha = 0.18f)
                                else if (isConnecting) NeonCyan.copy(alpha = 0.18f)
                                else CyberSurfaceVariant
                            )
                            .border(
                                1.dp,
                                if (isConnected) NeonEmerald
                                else if (isConnecting) NeonCyan
                                else CyberCardBorder,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = when {
                                isConnected -> Icons.Default.VerifiedUser
                                isConnecting -> Icons.Default.Bolt
                                else -> Icons.Default.Shield
                            },
                            contentDescription = "Status Icon",
                            tint = when {
                                isConnected -> NeonEmerald
                                isConnecting -> NeonCyan
                                else -> NeonOrange
                            },
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = when {
                            isConnected -> Strings.connectedSecureTitle(language)
                            isConnecting -> Strings.connectingStatusTitle(language)
                            else -> Strings.disconnectedTitle(language)
                        },
                        color = when {
                            isConnected -> NeonEmerald
                            isConnecting -> NeonCyan
                            else -> TextPrimary
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Security Badge Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isConnected) NeonEmerald.copy(alpha = 0.15f)
                            else if (isConnecting) NeonCyan.copy(alpha = 0.12f)
                            else CyberSurfaceVariant
                        )
                        .border(
                            1.dp,
                            if (isConnected) NeonEmerald.copy(alpha = 0.5f)
                            else if (isConnecting) NeonCyan.copy(alpha = 0.3f)
                            else CyberCardBorder,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = when {
                            isConnected -> "256-BIT SECURE"
                            isConnecting -> "CONNECTING"
                            else -> "UNPROTECTED"
                        },
                        color = when {
                            isConnected -> NeonEmerald
                            isConnecting -> NeonCyan
                            else -> NeonOrange
                        },
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Thin Cyber Divider Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(CyberCardBorder.copy(alpha = 0.5f))
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Single Clean IP Box (Compact, seamless transition)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isConnected) CyberSurfaceVariant else CyberDarkBg)
                    .border(
                        1.dp,
                        if (isConnected) NeonEmerald.copy(alpha = 0.5f) else CyberCardBorder,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 9.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = activeIp,
                        color = if (isConnected) NeonEmerald else TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(
                                if (isConnected) NeonEmerald.copy(alpha = 0.15f)
                                else NeonOrange.copy(alpha = 0.15f)
                            )
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isConnected) Strings.ipMaskedSecure(language) else Strings.ipExposedWarning(language),
                            color = if (isConnected) NeonEmerald else NeonOrange,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

