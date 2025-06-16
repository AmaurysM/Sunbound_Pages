package com.amaurysdelossantos.project.navigation.search

import com.amaurysdelossantos.project.navigation.finishedbooks.FinishedBooksComponent.FinishedBook
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//data class SearchResult(
//    val id: String,
//    val title: String,
//    val subtitle: String = "",
//    val type: SearchResultType
//)
//
//enum class SearchResultType {
//    BOOK, AUTHOR, TOPIC
//}
data class Book(
    val id: String,
    val title: String,
    val author: String?,
    val description: String = "",
)
class SearchComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

//
//
//    private val _searchResults = MutableValue<List<Book>>(emptyList())
//    val searchResults: Value<List<Book>> = _searchResults
//
//    private val _isLoading = MutableValue(false)
//    val isLoading: Value<Boolean> = _isLoading
//
//    private val _currentQuery = MutableValue("")
//    val currentQuery: Value<String> = _currentQuery
//
//    //private val coroutineScope = CoroutineScope(Dispatchers.Main)
//    private var searchJob: Job? = null
//
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

//    fun onSearch(query: String) {
//        searchJob?.cancel()
//        if (query.isNotBlank()) {
//            coroutineScope.launch {
//                performSearch(query)
//            }
//        }
//    }

//    fun onResultSelected(result: SearchResult) {
//        // Handle result selection - navigate or perform action
//        // You can add navigation logic here
//        println("Selected: ${result.title}")
//    }

//    private suspend fun performSearch(query: String) {
//        _isLoading.value = true
//
//        try {
//            // Simulate API call or database search
//            delay(200) // Simulate network delay
//
//            // Mock search results - replace with your actual search logic
//            val results = mockSearch(query)
//            _searchResults.value = results
//        } catch (e: Exception) {
//            _searchResults.value = emptyList()
//            // Handle error
//        } finally {
//            _isLoading.value = false
//        }
//    }



    // Mock search function - replace with your actual search implementation
//    private fun mockSearch(query: String): List<SearchResult> {
//        val mockData =
//
//        return mockData.filter {
//            it.title.contains(query, ignoreCase = true) ||
//                    it.subtitle.contains(query, ignoreCase = true)
//        }
//    }
}