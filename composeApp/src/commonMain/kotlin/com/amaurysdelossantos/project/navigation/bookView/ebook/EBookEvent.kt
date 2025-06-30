package com.amaurysdelossantos.project.navigation.bookView.ebook

sealed class EBookEvent {
    object StartBook : EBookEvent()
    object Retry : EBookEvent()
    object NavigateBack : EBookEvent()
}