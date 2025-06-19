package com.amaurysdelossantos.project.navigation.onMyDevice

import com.amaurysdelossantos.project.navigation.finishedbooks.FinishedBooksEvent
import com.amaurysdelossantos.project.navigation.search.Book
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnMyDeviceComponent (
    componentContext: ComponentContext,
    private val onBack: () -> Unit
) : ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _allBooks = listOf(
        Book("1", "The Great Gatsby", "F. Scott Fitzgerald"),
        Book("2", "To Kill a Mockingbird", "Harper Lee"),
        Book("3", "1984", "George Orwell"),
        Book("4", "Pride and Prejudice", "Jane Austen"),
        Book("5", "The Catcher in the Rye", "J.D. Salinger"),
        Book("6", "George Orwell", "Author"),
        Book("7", "Jane Austen", "Author"),
        Book("8", "Science Fiction", "Genre"),
        Book("9", "Classic Literature", "Genre"),
    )

    private val _filteredBooks = MutableStateFlow(_allBooks)
    val filteredBooks: StateFlow<List<Book>> = _filteredBooks

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
        }
    }

    fun cancelSearch(){
        _searchQuery.value = ""
        _filteredBooks.value = _allBooks
    }

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

}