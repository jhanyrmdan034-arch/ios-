package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.Strings
import com.example.model.AppLanguage
import com.example.model.VpnState
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun CyberConnectButton(
    vpnState: VpnState,
    language: AppLanguage,
    onConnectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    val buttonScale = remember { Animatable(1f) }

    // Infinite transitions for pulsing and rotating
    val infiniteTransition = rememberInfiniteTransition(label = "cyber_pulse")

    val pulse1 by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse1"
    )

    val pulseAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha1"
    )

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotating_ring"
    )

    val mainButtonColor = when (vpnState) {
        VpnState.DISCONNECTED -> listOf(CyberSurfaceVariant, CyberSurface)
        VpnState.CONNECTING -> listOf(NeonBlue, NeonPurple)
        VpnState.CONNECTED -> listOf(NeonCyan, NeonEmerald)
        VpnState.DISCONNECTING -> listOf(NeonPink, NeonPurple)
    }

    val glowColor = when (vpnState) {
        VpnState.DISCONNECTED -> Color(0xFF1E293B)
        VpnState.CONNECTING -> NeonBlue
        VpnState.CONNECTED -> NeonCyan
        VpnState.DISCONNECTING -> NeonPink
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(190.dp)
        ) {
            // Concentric Pulse Rings when Connected or Connecting
            if (vpnState == VpnState.CONNECTED || vpnState == VpnState.CONNECTING) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(pulse1)
                ) {
                    drawCircle(
                        color = glowColor.copy(alpha = pulseAlpha1 * 0.35f),
                        radius = size.minDimension / 2.1f
                    )
                }
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(pulse1 * 0.82f)
                ) {
                    drawCircle(
                        color = glowColor.copy(alpha = pulseAlpha1 * 0.5f),
                        radius = size.minDimension / 2.2f
                    )
                }
            }

            // Outer Orbit Track with tick marks or spinning dash
            Canvas(
                modifier = Modifier
                    .size(170.dp)
                    .rotate(if (vpnState == VpnState.CONNECTING) rotationAngle else 0f)
            ) {
                drawCircle(
                    color = CyberSurfaceVariant.copy(alpha = 0.8f),
                    radius = size.minDimension / 2 - 4.dp.toPx(),
                    style = Stroke(width = 2.dp.toPx())
                )

                if (vpnState == VpnState.CONNECTING) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(Color.Transparent, NeonBlue, NeonCyan, Color.Transparent)
                        ),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                } else if (vpnState == VpnState.CONNECTED) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(NeonCyan, NeonEmerald, NeonBlue, NeonCyan)
                        ),
                        radius = size.minDimension / 2 - 4.dp.toPx(),
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }

            // Main Interactive Button Circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(130.dp)
                    .scale(buttonScale.value)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = mainButtonColor,
                            center = Offset.Unspecified,
                            radius = 210f
                        )
                    )
                    .border(
                        width = 2.5.dp,
                        brush = Brush.linearGradient(
                            colors = if (vpnState == VpnState.CONNECTED) {
                                listOf(NeonCyan, NeonEmerald)
                            } else if (vpnState == VpnState.CONNECTING) {
                                listOf(NeonBlue, NeonPurple)
                            } else {
                                listOf(Color(0xFF334155), Color(0xFF1E293B))
                            }
                        ),
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        scope.launch {
                            buttonScale.animateTo(0.92f, tween(80))
                            buttonScale.animateTo(1f, tween(140))
                        }
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
                        } catch (_: Exception) {}
                        onConnectClick()
                    }
            ) {
                // Inner button icon & power symbol
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = when (vpnState) {
                            VpnState.CONNECTED -> Icons.Default.Security
                            VpnState.CONNECTING -> Icons.Default.Lock
                            else -> Icons.Default.PowerSettingsNew
                        },
                        contentDescription = "VPN Power",
                        tint = when (vpnState) {
                            VpnState.CONNECTED -> Color.Black
                            VpnState.CONNECTING -> Color.White
                            else -> NeonCyan
                        },
                        modifier = Modifier.size(46.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // State Text & Subtitle
        Text(
            text = when (vpnState) {
                VpnState.DISCONNECTED -> Strings.stateDisconnected(language)
                VpnState.CONNECTING -> Strings.stateConnecting(language)
                VpnState.CONNECTED -> Strings.stateConnected(language)
                VpnState.DISCONNECTING -> Strings.stateDisconnecting(language)
            },
            color = when (vpnState) {
                VpnState.CONNECTED -> NeonCyan
                VpnState.CONNECTING -> NeonBlue
                VpnState.DISCONNECTING -> NeonPink
                else -> TextSecondary
            },
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = when (vpnState) {
                VpnState.CONNECTED -> Strings.tapToDisconnect(language)
                VpnState.CONNECTING -> Strings.connectingTunnel(language)
                VpnState.DISCONNECTING -> Strings.stateDisconnecting(language)
                else -> Strings.tapToConnect(language)
            },
            color = TextSecondary.copy(alpha = 0.8f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
