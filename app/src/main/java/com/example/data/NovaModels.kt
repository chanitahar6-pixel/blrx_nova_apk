package com.example.data

data class NovaSpec(
    val label: String,
    val value: String
)

data class NovaItem(
    val id: String,
    val title: String,
    val category: String,
    val tagline: String,
    val price: String,
    val edition: String,
    val rating: Float,
    val imageResId: Int,
    val description: String,
    val specs: List<NovaSpec>,
    val isFavorite: Boolean = false,
    val isConnected: Boolean = false,
    val batteryLevel: Int = 0
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean
)

data class UserProfile(
    val name: String,
    val email: String,
    val tier: String,
    val memberCode: String,
    val savedCount: Int,
    val activeDevices: Int,
    val novaPoints: String
)

enum class NovaNavigationTab {
    HOME,
    EXPLORE,
    FAVORITES,
    PROFILE
}
