package com.example.ui.screens

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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material.icons.rounded.BatteryFull
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.NovaItem
import com.example.ui.components.NovaGlassCard
import com.example.ui.components.NovaPrimaryButton
import com.example.ui.components.NovaSectionHeader
import com.example.ui.components.NovaTopBar
import com.example.ui.theme.NovaBackground
import com.example.ui.theme.NovaGlassBorder
import com.example.ui.theme.NovaGlassGlowBorder
import com.example.ui.theme.NovaSuccess
import com.example.ui.theme.NovaSurface
import com.example.ui.theme.NovaSurfaceElevated
import com.example.ui.theme.NovaSurfaceSubtle
import com.example.ui.theme.NovaTextMuted
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.theme.NovaVioletGlow
import com.example.ui.theme.NovaVioletLight
import com.example.ui.theme.NovaVioletNeon
import com.example.ui.theme.NovaVioletPrimary

private data class QuickAction(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun HomeScreen(
    items: List<NovaItem>,
    unreadNotifications: Int,
    onNotificationClick: () -> Unit,
    onItemClick: (NovaItem) -> Unit,
    onQuickActionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val heroItem = items.firstOrNull { it.id == "nova_aura_pro" } ?: items.firstOrNull()

    val quickActions = listOf(
        QuickAction("Acoustic Lab", "Bespoke EQ Profile", Icons.Outlined.GraphicEq, "eq"),
        QuickAction("Neural Sync", "Realtime Synapse 2kHz", Icons.Outlined.Sensors, "neural"),
        QuickAction("Ambient Shield", "-48dB Active Null", Icons.Outlined.Layers, "anc"),
        QuickAction("Vault Key", "Privilege NV-8802", Icons.Outlined.VpnKey, "vault")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackground)
    ) {
        // Top Bar
        NovaTopBar(
            greeting = "Welcome back",
            userName = "Mounir",
            unreadNotifications = unreadNotifications,
            onNotificationClick = onNotificationClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Hero Card
            item {
                if (heroItem != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        // Large Hero Card with Dark Gradient & Subtle Violet Glow
                        NovaGlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("main_hero_card"),
                            shape = RoundedCornerShape(26.dp),
                            hasGlow = true
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                NovaVioletGlow.copy(alpha = 0.22f),
                                                NovaSurfaceElevated,
                                                NovaSurface
                                            )
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                // Status header inside hero card
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Live Status Badge
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(NovaSurfaceSubtle)
                                            .border(BorderStroke(1.dp, NovaGlassBorder), RoundedCornerShape(20.dp))
                                            .padding(horizontal = 10.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(if (heroItem.isConnected) NovaSuccess else NovaTextMuted)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (heroItem.isConnected) "AURA LINK ACTIVE" else "STANDBY READY",
                                            color = if (heroItem.isConnected) NovaTextPrimary else NovaTextSecondary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            letterSpacing = 0.5.sp
                                        )
                                    }

                                    // Battery / Signal info
                                    if (heroItem.isConnected) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Rounded.BatteryFull,
                                                contentDescription = null,
                                                tint = NovaVioletLight,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${heroItem.batteryLevel}%",
                                                color = NovaVioletLight,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Visual Representation
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(NovaBackground)
                                        .clickable { onItemClick(heroItem) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = heroItem.imageResId),
                                        contentDescription = heroItem.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    // Soft violet gradient overlay for visual unity
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    listOf(
                                                        Color.Transparent,
                                                        NovaBackground.copy(alpha = 0.65f)
                                                    )
                                                )
                                            )
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Product Title & Category
                                Text(
                                    text = heroItem.category.uppercase(),
                                    color = NovaVioletNeon,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = heroItem.title,
                                    color = NovaTextPrimary,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.3).sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = heroItem.tagline,
                                    color = NovaTextSecondary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                // Clear CTA Button
                                NovaPrimaryButton(
                                    text = if (heroItem.isConnected) "Control Holographic Audio" else "Connect AURA System",
                                    onClick = { onItemClick(heroItem) },
                                    modifier = Modifier.fillMaxWidth(),
                                    icon = Icons.Rounded.Tune,
                                    testTag = "hero_cta_button"
                                )
                            }
                        }
                    }
                }
            }

            // Quick Function Cards Header
            item {
                NovaSectionHeader(
                    title = "System Modules",
                    subtitle = "Quick calibration & sensory tools"
                )
            }

            // Grid of smaller function cards below hero card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionCard(
                            action = quickActions[0],
                            modifier = Modifier.weight(1f),
                            onClick = { onQuickActionClick(quickActions[0].title) }
                        )
                        QuickActionCard(
                            action = quickActions[1],
                            modifier = Modifier.weight(1f),
                            onClick = { onQuickActionClick(quickActions[1].title) }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionCard(
                            action = quickActions[2],
                            modifier = Modifier.weight(1f),
                            onClick = { onQuickActionClick(quickActions[2].title) }
                        )
                        QuickActionCard(
                            action = quickActions[3],
                            modifier = Modifier.weight(1f),
                            onClick = { onQuickActionClick(quickActions[3].title) }
                        )
                    }
                }
            }

            // Spotlight Showcase
            item {
                Spacer(modifier = Modifier.height(8.dp))
                NovaSectionHeader(
                    title = "Curated Atelier",
                    subtitle = "Limited release architectural hardware"
                )
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp)
                ) {
                    items(items.drop(1), key = { it.id }) { item ->
                        SpotlightCard(
                            item = item,
                            onClick = { onItemClick(item) }
                        )
                    }
                }
            }

            // Bottom spacing so content never gets covered by the bottom bar
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    action: QuickAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NovaGlassCard(
        modifier = modifier
            .testTag("quick_action_${action.tag}"),
        shape = RoundedCornerShape(18.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(NovaSurfaceSubtle)
                    .border(BorderStroke(1.dp, NovaGlassBorder), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.title,
                    tint = NovaVioletLight,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = action.title,
                color = NovaTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = action.subtitle,
                color = NovaTextMuted,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun SpotlightCard(
    item: NovaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NovaGlassCard(
        modifier = modifier
            .width(220.dp)
            .testTag("spotlight_card_${item.id}"),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(NovaBackground)
            ) {
                Image(
                    painter = painterResource(id = item.imageResId),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.category.uppercase(),
                color = NovaVioletNeon,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.title,
                color = NovaTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.price,
                    color = NovaVioletLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "View",
                    color = NovaTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
