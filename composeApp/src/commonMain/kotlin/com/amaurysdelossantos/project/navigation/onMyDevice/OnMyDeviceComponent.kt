package com.amaurysdelossantos.project.navigation.onMyDevice

import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.database.enums.MediaType
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
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig

class OnMyDeviceComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    private val bookDao: BookDao,
) : ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private lateinit var documentManager: DocumentManager

    private val logger = Logger(config = StaticConfig())

    fun bindDocumentManager(manager: DocumentManager) {
        this.documentManager = manager
    }

    val filteredBooks: StateFlow<List<Book>> = combine(
        bookDao.getAllBooks(),
        _searchQuery
    ) { books, query ->
        if (query.isBlank()) books
        else books.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.descriptor?.contains(query, ignoreCase = true) == true
        }
    }.stateIn(
        coroutineScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun onEvent(event: OnMyDeviceEvent) {
        when (event) {
            is OnMyDeviceEvent.SearchQueryChanged -> {
                onSearchQueryChanged(event.query);
            }

            OnMyDeviceEvent.BackClicked -> {
                onBack()
            }

            OnMyDeviceEvent.CancelSearch -> {
                cancelSearch()
            }

            OnMyDeviceEvent.OpenFileExplorer -> {
                // Platform-specific file picker logic (expect/actual or passed-in callback)
                println("TODO: Launch file explorer")
                documentManager.launch()

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
            val bytes = doc.toByteArray() ?: return@launch
            val name = doc.fileName() ?: "Untitled"
            val format = doc.bookFormat() ?: return@launch

            logger.e { "Format: $format , Name: $name, Bytes: ${bytes.size}, Doc: ${doc.toString()}" }

            val book = Book(
                title = name,
                descriptor = "Imported from file",
                format = format,
                mediaType = format.toMediaType(),
                filePath = doc.toString(),
            )

            bookDao.insertBook(book)
        }
    }

}