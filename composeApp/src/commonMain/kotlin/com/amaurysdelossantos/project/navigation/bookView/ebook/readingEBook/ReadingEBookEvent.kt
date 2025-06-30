package com.amaurysdelossantos.project.navigation.bookView.ebook.readingEBook

interface ReadingEBookEvent {
     object OnBackPressed : ReadingEBookEvent
     object OnSettingsClicked : ReadingEBookEvent
     object OnFontSizeChanged : ReadingEBookEvent
     object OnDarkModeChanged : ReadingEBookEvent
     object OnLineSpacingChanged : ReadingEBookEvent
     object OnCloseSettings : ReadingEBookEvent
}