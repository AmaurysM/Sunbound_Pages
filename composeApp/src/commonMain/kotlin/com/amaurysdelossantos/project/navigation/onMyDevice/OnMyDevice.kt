package com.amaurysdelossantos.project.navigation.onMyDevice

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amaurysdelossantos.project.customComposables.BookItem
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.util.rememberDocumentManager
import org.jetbrains.compose.resources.painterResource
import sunboundpages.composeapp.generated.resources.Res
import sunboundpages.composeapp.generated.resources.cancel
import sunboundpages.composeapp.generated.resources.chevron_left
import sunboundpages.composeapp.generated.resources.library
import sunboundpages.composeapp.generated.resources.more_horiz
import sunboundpages.composeapp.generated.resources.search

@Composable
fun OnMyDevice(
    component: OnMyDeviceComponent,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant
    val searchQuery = component.searchQuery.collectAsState()
    val filteredBooks = component.filteredBooks.collectAsState()
    val isLoading = component.isLoading.collectAsState()

    val documentManager = rememberDocumentManager { sharedDoc ->
        sharedDoc?.let {
            component.handlePickedDocument(it)
        }
    }

    LaunchedEffect(Unit) {
        component.bindDocumentManager(documentManager)
    }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Top Bar
        TopBar(
            iconColor = iconColor,
            onBackClicked = { component.onEvent(OnMyDeviceEvent.BackClicked) }
        )

        // Search Bar
        SearchBar(
            searchQuery = searchQuery.value,
            iconColor = iconColor,
            onSearchQueryChanged = { component.onSearchQueryChanged(it) },
            onCancelSearch = { component.onEvent(OnMyDeviceEvent.CancelSearch) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Content Area
        ContentArea(
            isLoading = isLoading.value,
            filteredBooks = filteredBooks.value,
            searchQuery = searchQuery.value,
            onOpenFileExplorer = { component.onEvent(OnMyDeviceEvent.OpenFileExplorer) },
            onBookClick = { book ->
                // Handle book click - you can add navigation logic here
                println("Book clicked: ${book.title}")
            }
        )
    }
}

@Composable
private fun TopBar(
    iconColor: Color,
    onBackClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(Res.drawable.chevron_left),
            contentDescription = "Back button",
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.clickable { onBackClicked() }
        )

        Text(
            text = "On My Device",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Image(
            painter = painterResource(Res.drawable.more_horiz),
            contentDescription = "Show More Button",
            colorFilter = ColorFilter.tint(iconColor)
        )
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    iconColor: Color,
    onSearchQueryChanged: (String) -> Unit,
    onCancelSearch: () -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        placeholder = { Text("Search by book title") },
        singleLine = true,
        leadingIcon = {
            Image(
                painter = painterResource(Res.drawable.search),
                contentDescription = "Search Icon",
                colorFilter = ColorFilter.tint(iconColor)
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                Image(
                    painter = painterResource(Res.drawable.cancel),
                    contentDescription = "Cancel Search",
                    colorFilter = ColorFilter.tint(iconColor),
                    modifier = Modifier.clickable { onCancelSearch() }
                )
            }
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun ContentArea(
    isLoading: Boolean,
    filteredBooks: List<Book>,
    searchQuery: String,
    onOpenFileExplorer: () -> Unit,
    onBookClick: (Book) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        when {
            isLoading -> {
                LoadingState()
            }

            filteredBooks.isEmpty() && searchQuery.isNotEmpty() -> {
                EmptySearchState()
            }

            filteredBooks.isEmpty() -> {
                EmptyLibraryState(onOpenFileExplorer = onOpenFileExplorer)
            }

            else -> {
                BookGrid(
                    books = filteredBooks,
                    onBookClick = onBookClick
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading your books...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptySearchState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.search),
                contentDescription = "No search results",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No books found",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Try adjusting your search terms",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EmptyLibraryState(
    onOpenFileExplorer: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.library),
                contentDescription = "Empty library",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Your library is empty",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add books to your device to see them here",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onOpenFileExplorer,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text("Add Books")
            }
        }
    }
}

@Composable
private fun BookGrid(
    books: List<Book>,
    onBookClick: (Book) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(books.size) { book ->
            BookItem(
                book = books[book],
                onClick = { onBookClick(books[book]) }
            )
        }
    }
}

//@Composable
//private fun BookItem(
//    book: Book,
//    onItemClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier
//            .clip(RoundedCornerShape(12.dp))
//            .background(MaterialTheme.colorScheme.surfaceVariant)
//            .clickable { onItemClick() }
//            .padding(8.dp)
//            .fillMaxWidth()
//    ) {
//        // Book cover placeholder
//        Box(
//            modifier = Modifier
//                .height(140.dp)
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(8.dp))
//                .background(MaterialTheme.colorScheme.secondaryContainer),
//            contentAlignment = Alignment.Center
//        ) {
//            Image(
//                painter = painterResource(Res.drawable.library),
//                contentDescription = "Book cover",
//                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
//                modifier = Modifier.size(32.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = book.title,
//            style = MaterialTheme.typography.bodyMedium,
//            fontWeight = FontWeight.Medium,
//            color = MaterialTheme.colorScheme.onSurface,
//            maxLines = 2,
//            minLines = 2
//        )
//    }
//}