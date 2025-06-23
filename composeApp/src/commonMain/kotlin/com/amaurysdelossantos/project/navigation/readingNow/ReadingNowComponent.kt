package com.amaurysdelossantos.project.navigation.readingNow

import com.arkivanov.decompose.ComponentContext

class ReadingNowComponent(
    componentContext: ComponentContext,
    private val onBookClicked: (String) -> Unit,
    private val onSeeMore: () -> Unit
) : ComponentContext by componentContext {

    fun onEvent(event: ReadingNowEvent) {
        when (event) {
            is ReadingNowEvent.ClickBook -> onBookClicked(event.bookId)
            ReadingNowEvent.SeeAllFinished -> onSeeMore()
        }
    }
}