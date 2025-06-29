package com.amaurysdelossantos.project.navigation.reading

sealed interface ReadingEvent {

    data class ClickBook(val bookId: String) : ReadingEvent
    data object SeeAllFinished : ReadingEvent
}