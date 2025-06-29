package com.amaurysdelossantos.project.navigation.bookView.ebook

//import com.amaurysdelossantos.project.util.NavigationHolder.navigation
import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.model.Book
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EBookViewComponent(
    componentContext: ComponentContext,
    private val bookId: String,
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

    fun onEvent(event: EBookEvent) {
        when (event) {
            EBookEvent.StartBook -> {
                //navigation.bringToFront(Configuration.ReadingEBookView(book.value?.id.toString()))
            }
        }
    }
}