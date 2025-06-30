package com.amaurysdelossantos.project.util

import com.amaurysdelossantos.project.navigation.RootComponent.Configuration
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.slide

fun resolveAnimation(
    from: Configuration?,
    to: Configuration,
    isForward: Boolean = true
): StackAnimator {

    // If we don't have a from configuration, use fade
    if (from == null) return fade()

    // Check for specific animation patterns based on navigation hierarchy
    return when {
        // Library to OnMyDevice navigation
        isLibraryToOnMyDevice(from, to) -> slide()

        // OnMyDevice to BookView navigation
        isOnMyDeviceToBookView(from, to) -> slide()

        // BookView to EBookView navigation
        isBookViewToEBookView(from, to) -> slide()

        // BookView to ComicView navigation
        isBookViewToComicView(from, to) -> slide()

        // EBookView to ReadingEBookView navigation
        isEBookViewToReadingEBookView(from, to) -> slide()

        // Reading to ReadingEBookView navigation
        isReadingToReadingEBookView(from, to) -> slide()

        // Reading to FinishedBooks navigation
        isReadingToFinishedBooks(from, to) -> slide()

        // Any other BookView related navigation
        isBookViewRelated(from, to) -> slide()

        // Default to fade for other transitions
        else -> fade()
    }
}

private fun isLibraryToOnMyDevice(from: Configuration, to: Configuration): Boolean {
    return (from is Configuration.Library && to is Configuration.Downloads.OnMyDevice) ||
            (from is Configuration.Downloads.OnMyDevice && to is Configuration.Library)
}

private fun isOnMyDeviceToBookView(from: Configuration, to: Configuration): Boolean {
    return (from is Configuration.Downloads.OnMyDevice && to is Configuration.BookView) ||
            (from is Configuration.BookView && to is Configuration.Downloads.OnMyDevice)
}

private fun isBookViewToEBookView(from: Configuration, to: Configuration): Boolean {
    return (from is Configuration.BookView && to is Configuration.BookView.EBookView) ||
            (from is Configuration.BookView.EBookView && to is Configuration.BookView)
}

private fun isBookViewToComicView(from: Configuration, to: Configuration): Boolean {
    return (from is Configuration.BookView && to is Configuration.BookView.ComicView) ||
            (from is Configuration.BookView.ComicView && to is Configuration.BookView)
}

private fun isEBookViewToReadingEBookView(from: Configuration, to: Configuration): Boolean {
    return (from is Configuration.BookView.EBookView && to is Configuration.BookView.EBookView.ReadingEBookView) ||
            (from is Configuration.BookView.EBookView.ReadingEBookView && to is Configuration.BookView.EBookView)
}

private fun isReadingToReadingEBookView(from: Configuration, to: Configuration): Boolean {
    return (from is Configuration.Reading && to is Configuration.BookView.EBookView.ReadingEBookView) ||
            (from is Configuration.BookView.EBookView.ReadingEBookView && to is Configuration.Reading)
}

private fun isReadingToFinishedBooks(from: Configuration, to: Configuration): Boolean {
    return (from is Configuration.Reading && to is Configuration.Reading.FinishedBooks) ||
            (from is Configuration.Reading.FinishedBooks && to is Configuration.Reading)
}

private fun isBookViewRelated(from: Configuration, to: Configuration): Boolean {
    return from is Configuration.BookView || to is Configuration.BookView ||
            from is Configuration.BookView.EBookView || to is Configuration.BookView.EBookView ||
            from is Configuration.BookView.ComicView || to is Configuration.BookView.ComicView ||
            from is Configuration.BookView.EBookView.ReadingEBookView || to is Configuration.BookView.EBookView.ReadingEBookView
}