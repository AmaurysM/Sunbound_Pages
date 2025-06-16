package com.amaurysdelossantos.project.navigation.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaurysdelossantos.project.navigation.downloads.Downloads
import com.amaurysdelossantos.project.navigation.finishedbooks.FinishedBookItem
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.jetbrains.compose.resources.painterResource
import sunboundpages.composeapp.generated.resources.Res
import sunboundpages.composeapp.generated.resources.cancel
import sunboundpages.composeapp.generated.resources.chevron_right
import sunboundpages.composeapp.generated.resources.library
import sunboundpages.composeapp.generated.resources.radar
import sunboundpages.composeapp.generated.resources.search

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    modifier: Modifier = Modifier,
    component: SearchComponent
) {

    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant

    // Subscribe to component state
    // val searchResults by component.searchResults.subscribeAsState()
    // val isLoading by component.isLoading.subscribeAsState()
    val searchQuery = component.searchQuery.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val filteredBooks = component.filteredBooks.collectAsState()
    // iOS-style background

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.Top
    ) {




            TextField(
                value = searchQuery.value,
                onValueChange = { q -> component.onSearchQueryChanged(q) },
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
                    Image(
                        painter = painterResource(Res.drawable.cancel),
                        contentDescription = "Cancel Search",
                        colorFilter = ColorFilter.tint(iconColor)
                    )
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp)
                    ),
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

            Spacer(modifier = Modifier.height(8.dp))

            // Finished Books List
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (filteredBooks.value.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No books found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredBooks.value.size) { book ->
                            SearchResultItem(result = filteredBooks.value[book], onItemClick = { })
                        }
                    }
                }
            }

    }
}

@Composable
private fun SearchResultItem(
    result: Book,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        Image(
            painter = painterResource(Res.drawable.library),
            contentDescription = "Book cover",
            modifier = Modifier
                .height(140.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = result.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2
        )
    }
}
