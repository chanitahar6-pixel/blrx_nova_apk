package com.example.data

import com.example.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NovaRepository {

    private val initialItems = listOf(
        NovaItem(
            id = "nova_aura_pro",
            title = "AURA ONE PRO",
            category = "Spatial Audio",
            tagline = "Acoustic Singularity & Spatial Holography",
            price = "$899",
            edition = "Signature No. 042 / 500",
            rating = 4.98f,
            imageResId = R.drawable.img_hero_device,
            description = "Crafted from aerospace titanium and precision acoustic composites. AURA ONE PRO integrates custom 50mm beryllium planar transducers with neural noise calibration to recreate an infinite holographic soundstage.",
            specs = listOf(
                NovaSpec("Transducer", "50mm Beryllium Planar"),
                NovaSpec("Frequency", "4 Hz – 52 kHz"),
                NovaSpec("Endurance", "64 Hours Active"),
                NovaSpec("Latency", "1.2ms Optical Sync"),
                NovaSpec("Chassis", "Grade 5 Titanium"),
                NovaSpec("Isolation", "-48dB Neural ANC")
            ),
            isFavorite = true,
            isConnected = true,
            batteryLevel = 92
        ),
        NovaItem(
            id = "nova_vision_x",
            title = "VISION HORIZON X",
            category = "Optics & Vision",
            tagline = "Micro-OLED Spatial Optics Architecture",
            price = "$1,450",
            edition = "Limited Studio Run",
            rating = 4.95f,
            imageResId = R.drawable.img_explore_device,
            description = "Ultralight dual 4K micro-OLED optical waveguides engineered with precision prism geometry. Seamlessly overlays minimal telemetry onto natural human vision with zero perceptible latency.",
            specs = listOf(
                NovaSpec("Display", "Dual 4K Micro-OLED"),
                NovaSpec("Field of View", "110° Holographic"),
                NovaSpec("Weight", "68 Grams"),
                NovaSpec("Processor", "NOVA Silicon V2"),
                NovaSpec("Optics", "Prismatic Waveguide"),
                NovaSpec("Finish", "Obsidian Matte Titanium")
            ),
            isFavorite = false,
            isConnected = false,
            batteryLevel = 0
        ),
        NovaItem(
            id = "nova_neural_core",
            title = "NEURAL MATRIX LINK",
            category = "Neural Tech",
            tagline = "Biometric Synaptic Telemetry Hub",
            price = "$2,100",
            edition = "Founders Reserve",
            rating = 4.99f,
            imageResId = R.drawable.img_nova_logo,
            description = "An ultra-compact solid-state neural sensor band that monitors cognitive flow states, heart coherence, and deep circadian rhythms with clinical grade sensitivity.",
            specs = listOf(
                NovaSpec("Sensor Array", "16-Channel Dry EMG"),
                NovaSpec("Sampling Rate", "2048 Hz Realtime"),
                NovaSpec("Material", "Monolithic Ceramic"),
                NovaSpec("Water Resistance", "50m Depth Proof"),
                NovaSpec("Telemetry", "Sub-Millivolt Precision"),
                NovaSpec("Battery", "14 Days Continuous")
            ),
            isFavorite = true,
            isConnected = false,
            batteryLevel = 0
        ),
        NovaItem(
            id = "nova_chrono_v",
            title = "CHRONO TITANIUM V",
            category = "Wearables",
            tagline = "Perpetual Ceramic & Sapphire Smart Chronometer",
            price = "$1,180",
            edition = "Collector Edition 180 / 300",
            rating = 4.92f,
            imageResId = R.drawable.img_hero_device,
            description = "Combining classic haute horlogerie with quantum micro-sensors. Enclosed in polished titanium carbide with an unscratchable synthetic sapphire crystal dome.",
            specs = listOf(
                NovaSpec("Case Diameter", "41.5mm"),
                NovaSpec("Crystal", "Curved Sapphire Dome"),
                NovaSpec("Haptics", "Linear Harmonic Motor"),
                NovaSpec("Water Proof", "10 ATM Certified"),
                NovaSpec("Strap", "Fluoroelastomer & Leather"),
                NovaSpec("Autonomy", "21 Days Hybrid Mode")
            ),
            isFavorite = false,
            isConnected = false,
            batteryLevel = 0
        ),
        NovaItem(
            id = "nova_stealth_dock",
            title = "STEALTH INDUCTION DOCK",
            category = "Limited Editions",
            tagline = "Monolithic Obsidian Magnetic Charging Pod",
            price = "$480",
            edition = "Numbered 076 / 250",
            rating = 4.89f,
            imageResId = R.drawable.img_explore_device,
            description = "Milled from a single solid block of black anodized aeronautical alloy with frosted smoked glass top. Delivers simultaneous resonant power to three NOVA devices with ambient aura glow.",
            specs = listOf(
                NovaSpec("Power Output", "100W GaN Fast Delivery"),
                NovaSpec("Thermal", "Cryo-Vapor Chamber"),
                NovaSpec("Materials", "Anodized Billet Alloy"),
                NovaSpec("Lighting", "Ambient Violet Aura"),
                NovaSpec("Channels", "3 Magnetic Coils"),
                NovaSpec("Weight", "840g Solid Base")
            ),
            isFavorite = false,
            isConnected = false,
            batteryLevel = 0
        )
    )

    private val _items = MutableStateFlow(initialItems)
    val items: StateFlow<List<NovaItem>> = _items.asStateFlow()

    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem(
                id = "n1",
                title = "Firmware Update 2.4.1",
                message = "AURA ONE PRO spatial fidelity package installed successfully.",
                timeAgo = "10m ago",
                isRead = false
            ),
            NotificationItem(
                id = "n2",
                title = "Exclusive Allocation",
                message = "Your priority queue for VISION HORIZON X is now active.",
                timeAgo = "2h ago",
                isRead = false
            ),
            NotificationItem(
                id = "n3",
                title = "NOVA Black Tier Welcome",
                message = "Your private concierge privileges are unlocked.",
                timeAgo = "1d ago",
                isRead = true
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            name = "Mounir Chani",
            email = "mounirchani2@gmail.com",
            tier = "Black Tier Founding Member",
            memberCode = "#NV-8802",
            savedCount = 2,
            activeDevices = 1,
            novaPoints = "2,450"
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun toggleFavorite(itemId: String) {
        _items.update { currentList ->
            currentList.map { item ->
                if (item.id == itemId) {
                    val updated = item.copy(isFavorite = !item.isFavorite)
                    updated
                } else item
            }
        }
        val count = _items.value.count { it.isFavorite }
        _userProfile.update { it.copy(savedCount = count) }
    }

    fun markNotificationsRead() {
        _notifications.update { list ->
            list.map { it.copy(isRead = true) }
        }
    }

    fun toggleDeviceConnection(itemId: String) {
        _items.update { list ->
            list.map { item ->
                if (item.id == itemId) {
                    val nextState = !item.isConnected
                    item.copy(
                        isConnected = nextState,
                        batteryLevel = if (nextState) 92 else 0
                    )
                } else item
            }
        }
        val activeCount = _items.value.count { it.isConnected }
        _userProfile.update { it.copy(activeDevices = activeCount) }
    }
}
