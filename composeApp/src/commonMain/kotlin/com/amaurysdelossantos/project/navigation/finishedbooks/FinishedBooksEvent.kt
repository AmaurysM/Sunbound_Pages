package com.amaurysdelossantos.project.navigation.finishedbooks

sealed interface FinishedBooksEvent {
    data class SearchQueryChanged(val query: String) : FinishedBooksEvent
    object BackClicked : FinishedBooksEvent
    object CancelSearch : FinishedBooksEvent
}