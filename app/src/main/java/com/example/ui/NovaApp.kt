package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.NovaNavigationTab
import com.example.ui.components.AboutDialog
import com.example.ui.components.NotificationBottomSheet
import com.example.ui.components.NovaBottomBar
import com.example.ui.components.PrivacyDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.NovaBackground
import com.example.ui.theme.NovaGlassBorder
import com.example.ui.theme.NovaSurfaceElevated
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaVioletLight
import com.example.ui.theme.NovaVioletNeon
import com.example.ui.theme.NovaVioletPrimary

@Composable
fun NovaApp(
    viewModel: NovaViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedDetailItem by viewModel.selectedDetailItem.collectAsStateWithLifecycle()
    val items by viewModel.items.collectAsStateWithLifecycle()
    val filteredExploreItems by viewModel.filteredExploreItems.collectAsStateWithLifecycle()
    val favoriteItems by viewModel.favoriteItems.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val unreadNotifications by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    val isNotificationSheetOpen by viewModel.isNotificationSheetOpen.collectAsStateWithLifecycle()
    val isSettingsDialogOpen by viewModel.isSettingsDialogOpen.collectAsStateWithLifecycle()
    val isAboutDialogOpen by viewModel.isAboutDialogOpen.collectAsStateWithLifecycle()
    val isPrivacyDialogOpen by viewModel.isPrivacyDialogOpen.collectAsStateWithLifecycle()
    val actionFeedback by viewModel.actionFeedback.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(actionFeedback) {
        val message = actionFeedback
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearFeedback()
        }
    }

    // Handle back button when inside detail view
    BackHandler(enabled = selectedDetailItem != null) {
        viewModel.closeDetail()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = NovaBackground,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = if (selectedDetailItem == null) 80.dp else 90.dp)
            ) { data ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NovaSurfaceElevated.copy(alpha = 0.95f))
                        .border(
                            BorderStroke(1.dp, NovaVioletPrimary.copy(alpha = 0.6f)),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = data.visuals.message,
                        color = NovaTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        bottomBar = {
            // Show bottom navigation bar only when not in detail view
            AnimatedVisibility(
                visible = selectedDetailItem == null,
                enter = fadeIn(tween(200)) + slideInVertically(tween(200)) { it },
                exit = fadeOut(tween(150)) + slideOutVertically(tween(150)) { it }
            ) {
                NovaBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        viewModel.selectTab(tab)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NovaBackground)
        ) {
            // Detail Screen or Tab Screens
            val detailItem = selectedDetailItem
            if (detailItem != null) {
                DetailScreen(
                    item = detailItem,
                    onBack = { viewModel.closeDetail() },
                    onFavoriteToggle = { id -> viewModel.toggleFavorite(id) },
                    onReserveClick = { item ->
                        viewModel.showFeedback("Priority allocation reserved for ${item.title}")
                    }
                )
            } else {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(220)) +
                                slideInHorizontally(animationSpec = tween(220)) { width ->
                                    if (targetState.ordinal > initialState.ordinal) width / 4 else -width / 4
                                })
                            .togetherWith(
                                fadeOut(animationSpec = tween(180)) +
                                        slideOutHorizontally(animationSpec = tween(180)) { width ->
                                            if (targetState.ordinal > initialState.ordinal) -width / 4 else width / 4
                                        }
                            )
                    },
                    label = "tabTransition"
                ) { tab ->
                    when (tab) {
                        NovaNavigationTab.HOME -> {
                            HomeScreen(
                                items = items,
                                unreadNotifications = unreadNotifications,
                                onNotificationClick = { viewModel.openNotifications() },
                                onItemClick = { item -> viewModel.openDetail(item) },
                                onQuickActionClick = { actionTitle ->
                                    viewModel.showFeedback("$actionTitle module initiated")
                                }
                            )
                        }

                        NovaNavigationTab.EXPLORE -> {
                            ExploreScreen(
                                items = filteredExploreItems,
                                searchQuery = searchQuery,
                                selectedCategory = selectedCategory,
                                onSearchChange = { query -> viewModel.updateSearchQuery(query) },
                                onCategorySelect = { cat -> viewModel.selectCategory(cat) },
                                onItemClick = { item -> viewModel.openDetail(item) },
                                onFavoriteToggle = { id -> viewModel.toggleFavorite(id) }
                            )
                        }

                        NovaNavigationTab.FAVORITES -> {
                            FavoritesScreen(
                                favoriteItems = favoriteItems,
                                onItemClick = { item -> viewModel.openDetail(item) },
                                onRemoveFavorite = { id -> viewModel.toggleFavorite(id) },
                                onExploreClick = { viewModel.selectTab(NovaNavigationTab.EXPLORE) }
                            )
                        }

                        NovaNavigationTab.PROFILE -> {
                            ProfileScreen(
                                profile = userProfile,
                                onSettingsClick = { viewModel.setSettingsOpen(true) },
                                onNotificationsClick = { viewModel.openNotifications() },
                                onPrivacyClick = { viewModel.setPrivacyOpen(true) },
                                onAboutClick = { viewModel.setAboutOpen(true) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Notification Bottom Sheet
    if (isNotificationSheetOpen) {
        NotificationBottomSheet(
            notifications = notifications,
            onDismiss = { viewModel.closeNotifications() }
        )
    }

    // Settings Dialog
    if (isSettingsDialogOpen) {
        SettingsDialog(
            onDismiss = { viewModel.setSettingsOpen(false) }
        )
    }

    // About Dialog
    if (isAboutDialogOpen) {
        AboutDialog(
            onDismiss = { viewModel.setAboutOpen(false) }
        )
    }

    // Privacy Dialog
    if (isPrivacyDialogOpen) {
        PrivacyDialog(
            onDismiss = { viewModel.setPrivacyOpen(false) }
        )
    }
}
