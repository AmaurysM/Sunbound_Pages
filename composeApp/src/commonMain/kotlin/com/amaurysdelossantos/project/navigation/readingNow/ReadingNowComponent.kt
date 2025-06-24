package com.amaurysdelossantos.project.navigation.readingNow

import com.amaurysdelossantos.project.database.dao.BookDao
import com.arkivanov.decompose.ComponentContext

class ReadingNowComponent(
    componentContext: ComponentContext,
    private val bookDao: BookDao,
    private val onBookClicked: (String) -> Unit,
    private val onSeeMore: () -> Unit
) : ComponentContext by componentContext {

    //    private val _allBooks = bookDao.getAllBooks()
//    val allBooks: Flow<List<Book>> = _allBooks
    //val allBooks: Flow<List<Book>> = bookDao.getAllBooks()
    val currentlyReading = bookDao.getCurrentlyReadingBooks()
    val recentlyOpened = bookDao.getRecentlyOpenedBooks()
    val finishedBooks = bookDao.getFinishedBooks()

    fun onEvent(event: ReadingNowEvent) {
        when (event) {
            is ReadingNowEvent.ClickBook -> onBookClicked(event.bookId)
            ReadingNowEvent.SeeAllFinished -> onSeeMore()
        }
    }
}