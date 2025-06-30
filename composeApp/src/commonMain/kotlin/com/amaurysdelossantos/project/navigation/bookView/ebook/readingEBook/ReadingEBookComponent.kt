package com.amaurysdelossantos.project.navigation.bookView.ebook.readingEBook

import co.touchlab.kermit.Logger
import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.database.enums.BookFormat
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.util.file_reader.readTextFile
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReadingEBookComponent(
    componentContext: ComponentContext,
    private val bookId: String,
    private val bookDao: BookDao,
    private val onBack: () -> Unit = {}
) : ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    private val _bookContent = MutableStateFlow<String?>(null)
    val bookContent: StateFlow<String?> = _bookContent

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadBook(bookId)
    }

    private fun loadBook(bookId: String) {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                val loadedBook = bookDao.getBookById(bookId)
                _book.value = loadedBook

                if (loadedBook != null) {
                    when (loadedBook.format) {
                        BookFormat.TXT -> {
                            val path = loadedBook.filePath
                            if (path.isBlank()) {
                                _bookContent.value = "Missing file path for book."
                                Logger.e { "Book path is null or blank for ID: $bookId" }
                                return@launch
                            }

                            val content = readTextFile(path)
                            _bookContent.value = content
                            Logger.d { "Successfully loaded TXT content: ${content?.take(100)}..." }
                        }

                        BookFormat.PDF -> {
                            _bookContent.value = "PDF format support is coming soon."
                        }

                        BookFormat.EPUB -> {
                            _bookContent.value = "EPUB format support is coming soon."
                        }

                        else -> {
                            _bookContent.value = "Format ${loadedBook.format} is not yet supported."
                        }
                    }
                } else {
                    _bookContent.value = "Book not found with ID: $bookId"
                    Logger.e { "Book not found with ID: $bookId" }
                }
            } catch (exception: Exception) {
                _bookContent.value = "Error loading book: ${exception.message}"
                Logger.e(exception) { "Error loading book content" }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onBackPressed() {
        onBack()
    }

    fun retryLoading() {
        loadBook(bookId)
    }

    fun saveBookmark(position: Int) {
        coroutineScope.launch {
            Logger.d { "Saving bookmark at position: $position" }
        }
    }

    fun updateReadingProgress(progress: Float) {
        coroutineScope.launch {
            _book.value?.let {
                Logger.d { "Updating reading progress: ${(progress * 100).toInt()}%" }
            }
        }
    }
}
