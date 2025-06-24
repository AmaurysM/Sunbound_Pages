package com.amaurysdelossantos.project.navigation.library

import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.util.DocumentManager
import com.amaurysdelossantos.project.util.SharedDocument
import com.amaurysdelossantos.project.util.toMediaType
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LibraryComponent(
    componentContext: ComponentContext,
    private val onMyDeviceButton: () -> Unit,
    private val bookDao: BookDao,

    ) : ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _allBooks = bookDao.getAllBooks()
    val allBooks: Flow<List<Book>> = _allBooks

    private lateinit var documentManager: DocumentManager

    fun bindDocumentManager(manager: DocumentManager) {
        this.documentManager = manager
    }

    fun onEvent(event: LibraryEvent) {
        when (event) {
            LibraryEvent.OnMyDevice -> {
                onMyDeviceButton()
            }

            LibraryEvent.ImportFromFiles -> documentManager.launch()


            LibraryEvent.WebServer -> TODO()
        }
    }

    fun handlePickedDocument(doc: SharedDocument) {
        coroutineScope.launch {
            val bytes = doc.toByteArray() ?: return@launch
            val name = doc.fileName() ?: "Untitled"
            val format = doc.bookFormat() ?: return@launch


            val book = Book(
                title = name,
                description = "Imported from file",
                format = format,
                mediaType = format.toMediaType(),
                filePath = doc.toString(),
            )

            bookDao.insertBook(book)
        }
    }
}