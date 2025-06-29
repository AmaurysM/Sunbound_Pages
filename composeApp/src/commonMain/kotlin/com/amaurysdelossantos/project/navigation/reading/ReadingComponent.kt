package com.amaurysdelossantos.project.navigation.reading

import com.amaurysdelossantos.project.database.dao.BookDao
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.take

class ReadingComponent(
    componentContext: ComponentContext,
    private val bookDao: BookDao,
    private val onBookClicked: (String) -> Unit,
    private val onSeeMore: () -> Unit
) : ComponentContext by componentContext {

    val currentlyReading = bookDao.getCurrentlyReadingBooks()
    val recentlyOpened = bookDao.getRecentlyOpenedBooks().take(5)
    val finishedBooks = bookDao.getFinishedBooks().take(10)

    fun onEvent(event: ReadingEvent) {
        when (event) {
            is ReadingEvent.ClickBook -> onBookClicked(event.bookId)
            ReadingEvent.SeeAllFinished -> onSeeMore()
        }
    }
}