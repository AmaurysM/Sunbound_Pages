package com.amaurysdelossantos.project.navigation.bookView.ebook

import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.model.Book
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class EBookViewComponent(
    componentContext: ComponentContext,
    private val bookId: String,
    private val bookDao: BookDao,
    private val readBook: (bookId: String) -> Unit = {}
) : ComponentContext by componentContext {

    // Use SupervisorJob to prevent cancellation issues
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadBook(bookId)
    }

    private fun loadBook(bookId: String) {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val bookResult = bookDao.getBookById(bookId)
                _book.value = bookResult

                if (bookResult == null) {
                    _error.value = "Book not found"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onEvent(event: EBookEvent) {
        when (event) {
            EBookEvent.StartBook -> {
                val currentBook = _book.value
                if (currentBook != null) {
                    readBook(currentBook.id)
                }
            }

            EBookEvent.Retry -> {
                loadBook(bookId)
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        // Cancel any ongoing operations when component is destroyed
//        coroutineScope.coroutineContext.cancel()
//    }
}