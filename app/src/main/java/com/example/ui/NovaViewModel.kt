package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.NovaItem
import com.example.data.NovaNavigationTab
import com.example.data.NovaRepository
import com.example.data.NotificationItem
import com.example.data.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NovaViewModel(
    private val repository: NovaRepository = NovaRepository()
) : ViewModel() {

    val items: StateFlow<List<NovaItem>> = repository.items
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val userProfile: StateFlow<UserProfile> = repository.userProfile

    private val _currentTab = MutableStateFlow(NovaNavigationTab.HOME)
    val currentTab: StateFlow<NovaNavigationTab> = _currentTab.asStateFlow()

    private val _selectedDetailItem = MutableStateFlow<NovaItem?>(null)
    val selectedDetailItem: StateFlow<NovaItem?> = _selectedDetailItem.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _isNotificationSheetOpen = MutableStateFlow(false)
    val isNotificationSheetOpen: StateFlow<Boolean> = _isNotificationSheetOpen.asStateFlow()

    private val _isSettingsDialogOpen = MutableStateFlow(false)
    val isSettingsDialogOpen: StateFlow<Boolean> = _isSettingsDialogOpen.asStateFlow()

    private val _isAboutDialogOpen = MutableStateFlow(false)
    val isAboutDialogOpen: StateFlow<Boolean> = _isAboutDialogOpen.asStateFlow()

    private val _isPrivacyDialogOpen = MutableStateFlow(false)
    val isPrivacyDialogOpen: StateFlow<Boolean> = _isPrivacyDialogOpen.asStateFlow()

    private val _actionFeedback = MutableStateFlow<String?>(null)
    val actionFeedback: StateFlow<String?> = _actionFeedback.asStateFlow()

    // Filtered explore items based on search and category
    val filteredExploreItems: StateFlow<List<NovaItem>> = combine(
        items,
        searchQuery,
        selectedCategory
    ) { allItems, query, category ->
        allItems.filter { item ->
            val matchesCategory = (category == "All") || (item.category.equals(category, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.category.contains(query, ignoreCase = true) ||
                    item.tagline.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteItems: StateFlow<List<NovaItem>> = items.combine(_selectedDetailItem) { allItems, _ ->
        allItems.filter { it.isFavorite }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = notifications.combine(_isNotificationSheetOpen) { list, _ ->
        list.count { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun selectTab(tab: NovaNavigationTab) {
        if (_selectedDetailItem.value != null) {
            _selectedDetailItem.value = null
        }
        _currentTab.value = tab
    }

    fun openDetail(item: NovaItem) {
        _selectedDetailItem.value = item
    }

    fun closeDetail() {
        _selectedDetailItem.value = null
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(itemId: String) {
        repository.toggleFavorite(itemId)
        // If current detail is this item, sync its favorite status
        val current = _selectedDetailItem.value
        if (current != null && current.id == itemId) {
            _selectedDetailItem.value = current.copy(isFavorite = !current.isFavorite)
        }
        val isNowFav = items.value.find { it.id == itemId }?.isFavorite != true
        showFeedback(if (isNowFav) "Removed from Private Vault" else "Added to Private Vault")
    }

    fun toggleDeviceConnection(itemId: String) {
        repository.toggleDeviceConnection(itemId)
        val current = _selectedDetailItem.value
        if (current != null && current.id == itemId) {
            val updated = items.value.find { it.id == itemId }
            if (updated != null) {
                _selectedDetailItem.value = updated
            }
        }
        val isConn = items.value.find { it.id == itemId }?.isConnected == true
        showFeedback(if (isConn) "Device disconnected" else "Device linked with Ultra-Low Latency")
    }

    fun openNotifications() {
        _isNotificationSheetOpen.value = true
        repository.markNotificationsRead()
    }

    fun closeNotifications() {
        _isNotificationSheetOpen.value = false
    }

    fun setSettingsOpen(open: Boolean) {
        _isSettingsDialogOpen.value = open
    }

    fun setAboutOpen(open: Boolean) {
        _isAboutDialogOpen.value = open
    }

    fun setPrivacyOpen(open: Boolean) {
        _isPrivacyDialogOpen.value = open
    }

    fun showFeedback(message: String) {
        _actionFeedback.value = message
    }

    fun clearFeedback() {
        _actionFeedback.value = null
    }
}
