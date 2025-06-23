package com.amaurysdelossantos.project.navigation.onMyDevice

import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.model.Book
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class OnMyDeviceComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    bookDao: BookDao

) : ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery


    val filteredBooks: StateFlow<List<Book>> = combine(
        bookDao.getAllBooks(),
        _searchQuery
    ) { books, query ->
        if (query.isBlank()) books
        else books.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.descriptor.contains(query, ignoreCase = true)
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
            }
        }
    }

    internal fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    private fun cancelSearch() {
        _searchQuery.value = ""
    }

}