package com.amaurysdelossantos.project.navigation.finishedbooks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amaurysdelossantos.project.customComposables.BookItem
import com.amaurysdelossantos.project.model.Book
import org.jetbrains.compose.resources.painterResource
import sunboundpages.composeapp.generated.resources.Res
import sunboundpages.composeapp.generated.resources.cancel
import sunboundpages.composeapp.generated.resources.chevron_left
import sunboundpages.composeapp.generated.resources.more_horiz
import sunboundpages.composeapp.generated.resources.search

@Composable
fun FinishedBooks(
    component: FinishedBooksComponent,
    innerPadding: PaddingValues = PaddingValues()
) {

    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant
    val searchQuery = component.searchQuery.collectAsState()
    val filteredBooks = component.filteredBooks.collectAsState(emptyList())

    Column(
        modifier = Modifier.padding(innerPadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Top Bar
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
                modifier = Modifier.clickable {
                    component.onEvent(FinishedBooksEvent.BackClicked)
                }
            )

            Text(
                text = "Finished",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Image(
                painter = painterResource(Res.drawable.more_horiz),
                contentDescription = "Show More Button",
                colorFilter = ColorFilter.tint(iconColor)
            )
        }

        TextField(
            value = searchQuery.value,
            onValueChange = { q ->
                component.onEvent(FinishedBooksEvent.SearchQueryChanged(q))
            },
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
                    colorFilter = ColorFilter.tint(iconColor),
                    modifier = Modifier.clickable {
                        component.onEvent(FinishedBooksEvent.CancelSearch)
                    }
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
                filteredBooks.value.forEach { book ->
                    FinishedBookItem(book = book, onClick = {
                        component.onEvent(
                            FinishedBooksEvent.ClickBook("0")
                        )
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun FinishedBookItem(
    book: Book,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BookItem(
            book = book,
            onClick = { onClick }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = book.description.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    }
}
