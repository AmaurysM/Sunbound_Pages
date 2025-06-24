package com.amaurysdelossantos.project.navigation.finishedbooks

import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.model.Book
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

class FinishedBooksComponent(
    componentContext: ComponentContext,
    private val bookDao: BookDao,
    private val onBack: () -> Unit,
    private val onBookClicked: (String) -> Unit,

    ) : ComponentContext by componentContext {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()

    val filteredBooks: Flow<List<Book>> = combine(
        allBooks,
        searchQuery
    ) { books, query ->
        if (query.isBlank()) {
            books
        } else {
            books.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description?.contains(query, ignoreCase = true) == true
            }
        }
    }.flowOn(Dispatchers.Default)

    fun onEvent(event: FinishedBooksEvent) {
        when (event) {
            is FinishedBooksEvent.SearchQueryChanged -> onSearchQueryChanged(event.query)
            FinishedBooksEvent.BackClicked -> onBack()
            FinishedBooksEvent.CancelSearch -> cancelSearch()
            is FinishedBooksEvent.ClickBook -> onBookClicked(event.bookId)
        }
    }

    private fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    private fun cancelSearch() {
        _searchQuery.value = ""
    }

}
