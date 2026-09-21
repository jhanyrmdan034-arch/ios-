package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.Strings
import com.example.model.AppLanguage
import com.example.model.PingResult
import com.example.model.ServerLocation
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ServersScreen(
    servers: List<ServerLocation>,
    currentServer: ServerLocation,
    isSmartMode: Boolean,
    language: AppLanguage,
    isTestingPings: Boolean,
    pingResults: Map<String, PingResult>,
    onSelectServer: (ServerLocation) -> Unit,
    onSelectSmartAuto: () -> Unit,
    onTestAllPings: () -> Unit,
    onTestSinglePing: (ServerLocation) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("all") } // all, recommended, fastest, favorites

    val rotationTransition = rememberInfiniteTransition(label = "servers_spin")
    val spinAngle by rotationTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )

    val filteredServers = servers.filter { server ->
        val matchesQuery = searchQuery.isEmpty() ||
                server.countryEn.contains(searchQuery, ignoreCase = true) ||
                server.countryFa.contains(searchQuery, ignoreCase = true) ||
                server.cityEn.contains(searchQuery, ignoreCase = true) ||
                server.cityFa.contains(searchQuery, ignoreCase = true)

        val matchesTab = when (selectedTab) {
            "recommended" -> server.isRecommended
            "fastest" -> server.currentPingMs < 60
            "favorites" -> server.isFavorite
            else -> true
        }

        matchesQuery && matchesTab
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header Row with Title and Test All Pings button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = Strings.tabServers(language),
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${servers.size} ${Strings.allServers(language)}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            // Test All Pings Button
            Button(
                onClick = onTestAllPings,
                enabled = !isTestingPings,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyberSurfaceVariant,
                    contentColor = NeonCyan
                ),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                modifier = Modifier.height(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Test Pings",
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(if (isTestingPings) spinAngle else 0f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isTestingPings) Strings.pinging(language) else Strings.pingTest(language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = Strings.searchServers(language),
                    color = TextSecondary.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NeonCyan
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = TextSecondary
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = CyberCardBorder,
                focusedContainerColor = CyberSurface,
                unfocusedContainerColor = CyberSurface,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = selectedTab == "all",
                    onClick = { selectedTab = "all" },
                    label = { Text(Strings.allServers(language)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = CyberSurface,
                        labelColor = TextSecondary
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedTab == "recommended",
                    onClick = { selectedTab = "recommended" },
                    label = { Text(Strings.recommendedServers(language)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = CyberSurface,
                        labelColor = TextSecondary
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedTab == "fastest",
                    onClick = { selectedTab = "fastest" },
                    label = { Text(Strings.pingExcellent(language)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = CyberSurface,
                        labelColor = TextSecondary
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedTab == "favorites",
                    onClick = { selectedTab = "favorites" },
                    label = { Text(Strings.favoriteServers(language)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = CyberSurface,
                        labelColor = TextSecondary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Smart Auto Connect Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSmartMode) CyberSurfaceVariant else CyberSurface
            ),
            border = androidx.compose.foundation.BorderStroke(
                width = if (isSmartMode) 1.8.dp else 1.dp,
                brush = Brush.linearGradient(
                    if (isSmartMode) listOf(NeonCyan, NeonPurple) else listOf(CyberCardBorder, CyberCardBorder)
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectSmartAuto() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(NeonCyan, NeonBlue)))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Smart Auto",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = Strings.smartConnectTitle(language),
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeonCyan.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "AI OPTIMAL",
                                    color = NeonCyan,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = Strings.smartConnectDesc(language),
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                if (isSmartMode) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(NeonCyan)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Active",
                            tint = Color.Black,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Server List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(filteredServers, key = { it.id }) { server ->
                val isSelected = !isSmartMode && currentServer.id == server.id
                val pingInfo = pingResults[server.id]
                val isTestingThis = pingInfo?.isTesting == true
                val displayPing = pingInfo?.latencyMs ?: server.currentPingMs

                ServerItemRow(
                    server = server,
                    isSelected = isSelected,
                    displayPing = displayPing,
                    isTesting = isTestingThis,
                    language = language,
                    onSelect = { onSelectServer(server) },
                    onPingClick = { onTestSinglePing(server) },
                    onToggleFavorite = { onToggleFavorite(server.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
