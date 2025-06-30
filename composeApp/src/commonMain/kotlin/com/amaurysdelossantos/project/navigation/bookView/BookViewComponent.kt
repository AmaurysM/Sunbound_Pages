package com.amaurysdelossantos.project.navigation.bookView

import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.database.enums.MediaType
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.navigation.RootComponent.Configuration
import com.amaurysdelossantos.project.util.getBookFormat
import com.amaurysdelossantos.project.util.toMediaType
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookViewComponent(
    componentContext: ComponentContext,
    bookId: String = "0",
    private val bookDao: BookDao,
    private val toUniqueBookView: (Configuration) -> Unit = {}
) : ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    init {
        loadAndNavigate(bookId)
    }

    private fun loadAndNavigate(bookId: String) {
        coroutineScope.launch {
            val book = bookDao.getBookById(bookId)
            _book.value = book

            val format = getBookFormat(book?.filePath.orEmpty())
            val mediaType = format?.toMediaType()

            val uniqueMediaType = when (mediaType) {
                MediaType.EBOOK -> Configuration.BookView.EBookView(bookId)
                MediaType.COMIC -> Configuration.BookView.ComicView(bookId)
                //MediaType.AUDIOBOOK -> Configuration.BookView.AudioBookView(bookId)
                else -> Configuration.BookView.EBookView(bookId)
            }

            withContext(Dispatchers.Main) {
                toUniqueBookView(uniqueMediaType)
            }
        }
    }
}
