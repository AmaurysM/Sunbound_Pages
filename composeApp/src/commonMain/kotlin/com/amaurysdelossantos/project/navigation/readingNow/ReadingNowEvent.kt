package com.amaurysdelossantos.project.navigation.readingNow

sealed interface ReadingNowEvent {

    data class ClickBook(val bookId: String) : ReadingNowEvent
    data object SeeAllFinished : ReadingNowEvent
}