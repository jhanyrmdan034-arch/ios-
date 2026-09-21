package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.Strings
import com.example.model.AppLanguage
import com.example.model.VpnState
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class AppNavTab {
    HOME,
    SUPPORT,
    SERVERS,
    NATIONAL_TUNNEL
}

@Composable
fun CyberBottomBar(
    currentTab: AppNavTab,
    vpnState: VpnState,
    language: AppLanguage,
    onTabSelected: (AppNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(CyberSurface)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(listOf(CyberCardBorder, Color.Transparent)),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 1: Home (خانه)
            BottomNavItem(
                icon = Icons.Default.Home,
                label = Strings.tabHome(language),
                isSelected = currentTab == AppNavTab.HOME,
                hasBadge = false,
                onClick = { onTabSelected(AppNavTab.HOME) }
            )

            // Tab 2: Support (پشتیبانی) - Next to Home
            BottomNavItem(
                icon = Icons.Default.HeadsetMic,
                label = Strings.tabSupport(language),
                isSelected = currentTab == AppNavTab.SUPPORT,
                hasBadge = false,
                onClick = { onTabSelected(AppNavTab.SUPPORT) }
            )

            // Tab 3: Servers (سرورها)
            BottomNavItem(
                icon = Icons.Default.Dns,
                label = Strings.tabServers(language),
                isSelected = currentTab == AppNavTab.SERVERS,
                hasBadge = false,
                onClick = { onTabSelected(AppNavTab.SERVERS) }
            )

            // Tab 4: National Net Tunnel (تانل نت ملی)
            BottomNavItem(
                icon = Icons.Default.VpnLock,
                label = Strings.tabNationalTunnel(language),
                isSelected = currentTab == AppNavTab.NATIONAL_TUNNEL,
                hasBadge = true,
                badgeColor = if (vpnState == VpnState.CONNECTED) NeonEmerald else NeonCyan,
                onClick = { onTabSelected(AppNavTab.NATIONAL_TUNNEL) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    hasBadge: Boolean,
    badgeColor: Color = NeonCyan,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val tintColor by animateColorAsState(
        targetValue = if (isSelected) NeonCyan else TextSecondary,
        animationSpec = tween(250),
        label = "nav_tint"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) NeonCyan.copy(alpha = 0.12f) else Color.Transparent
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tintColor,
                    modifier = Modifier.size(24.dp)
                )

                if (hasBadge) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(badgeColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = label,
                color = tintColor,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
