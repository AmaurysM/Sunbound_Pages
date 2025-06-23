package com.amaurysdelossantos.project.navigation.bookInfo

import com.arkivanov.decompose.ComponentContext

class BookInfoComponent(
    componentContext: ComponentContext,
    bookId: String = "0",
    private val onBack: () -> Unit
) : ComponentContext by componentContext {

    val bookId: String = bookId;

    fun onEvent(event: BookInfoEvent) {
        when (event) {

            BookInfoEvent.BackClicked -> {
                onBack()
            }

        }
    }

}