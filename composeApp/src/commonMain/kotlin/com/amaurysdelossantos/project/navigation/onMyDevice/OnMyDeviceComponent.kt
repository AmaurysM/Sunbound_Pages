package com.amaurysdelossantos.project.navigation.onMyDevice

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.util.DocumentManager
import com.amaurysdelossantos.project.util.SharedDocument
import com.amaurysdelossantos.project.util.toMediaType
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OnMyDeviceComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    private val viewBook: (String) -> Unit,
    private val bookDao: BookDao,
) : ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private lateinit var documentManager: DocumentManager

    private val logger = Logger(config = StaticConfig())

    fun bindDocumentManager(manager: DocumentManager) {
        this.documentManager = manager
    }

    val filteredBooks: StateFlow<List<Book>> = combine(
        bookDao.getAllBooks(),
        _searchQuery
    ) { books, query ->
        // Update loading state when books are loaded
        _isLoading.value = false

        if (query.isBlank()) {
            books
        } else {
            books.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description?.contains(query, ignoreCase = true) == true
            }
        }
    }.stateIn(
        coroutineScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun onEvent(event: OnMyDeviceEvent) {
        when (event) {
            is OnMyDeviceEvent.SearchQueryChanged -> {
                onSearchQueryChanged(event.query)
            }

            OnMyDeviceEvent.BackClicked -> {
                onBack()
            }

            OnMyDeviceEvent.CancelSearch -> {
                cancelSearch()
            }

            OnMyDeviceEvent.OpenFileExplorer -> {
                try {
                    documentManager.launch()
                } catch (e: Exception) {
                    logger.e(e) { "Error launching file explorer" }
                }
            }

            is OnMyDeviceEvent.BookClicked -> {
                viewBook(event.bookId)
                //println("Book clicked: ${event.bookId}")
            }

            is OnMyDeviceEvent.BookDeleted -> {
                coroutineScope.launch {

                    bookDao.hardDeleteBook(event.bookId)

                }
            }
        }
    }

    internal fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    private fun cancelSearch() {
        _searchQuery.value = ""
    }

    fun handlePickedDocument(doc: SharedDocument) {
        coroutineScope.launch {
            try {
                val bytes = doc.toByteArray() ?: run {
                    logger.e { "Failed to read document bytes" }
                    return@launch
                }

                val name = doc.fileName() ?: "Untitled"
                val format = doc.bookFormat() ?: run {
                    logger.e { "Unsupported book format" }
                    return@launch
                }

                logger.d { "Processing document: Format=$format, Name=$name, Size=${bytes.size} bytes" }

                val book = Book(
                    title = name,
                    description = "Imported from file",
                    format = format,
                    mediaType = format.toMediaType(),
                    filePath = doc.toString(),
                )

                bookDao.insertBook(book)
                logger.i { "Successfully imported book: $name" }

            } catch (e: Exception) {
                logger.e(e) { "Error processing picked document" }
            }
        }
    }

}