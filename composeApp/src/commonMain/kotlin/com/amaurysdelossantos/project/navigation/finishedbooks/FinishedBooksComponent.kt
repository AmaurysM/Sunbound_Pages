package com.amaurysdelossantos.project.navigation.finishedbooks

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FinishedBooksComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _allBooks = List(20) { index ->
        FinishedBook("Book Title $index", "Description for Book $index")
    }

    private val _filteredBooks = MutableStateFlow(_allBooks)
    val filteredBooks: StateFlow<List<FinishedBook>> = _filteredBooks

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        coroutineScope.launch {
            _filteredBooks.value = if (newQuery.isBlank()) {
                _allBooks
            } else {
                _allBooks.filter {
                    it.title.contains(newQuery, ignoreCase = true) ||
                            it.description.contains(newQuery, ignoreCase = true)
                }
            }
        }
    }

    data class FinishedBook(
        val title: String,
        val description: String,
        val progress: Float = 1f
    )
}
