package com.amaurysdelossantos.project.database.enums

enum class WebtoonPanelType {
    STANDARD,      // Regular comic panel
    TITLE_CARD,    // Episode title or chapter intro
    SOUND_EFFECT,  // Panel primarily for sound effects
    TRANSITION,    // Scene transition panel
    ANIMATED,      // Animated panel (GIF/video)
    INTERACTIVE,   // Interactive panel requiring user input
    ADVERTISEMENT  // Ad panel (for monetized webtoons)
}