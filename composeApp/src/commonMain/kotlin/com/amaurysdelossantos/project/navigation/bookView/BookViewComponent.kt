package com.amaurysdelossantos.project.navigation.bookView

import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.model.Book
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewComponent(
    componentContext: ComponentContext,
    bookId: String = "0",
    private val onBack: () -> Unit,
    private val bookDao: BookDao,

    ) : ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    init {
        loadBook(bookId)
    }

    private fun loadBook(bookId: String) {
        coroutineScope.launch {
            _book.value = bookDao.getBookById(bookId)
        }
    }

    fun onEvent(event: BookViewEvent) {
        when (event) {

            BookViewEvent.BackClicked -> {
                onBack()
            }

        }
    }

}