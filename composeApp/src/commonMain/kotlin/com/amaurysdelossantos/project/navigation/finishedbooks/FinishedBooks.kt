package com.amaurysdelossantos.project.navigation.finishedbooks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import sunboundpages.composeapp.generated.resources.Res
import sunboundpages.composeapp.generated.resources.cancel
import sunboundpages.composeapp.generated.resources.chevron_left
import sunboundpages.composeapp.generated.resources.chevron_right
import sunboundpages.composeapp.generated.resources.library
import sunboundpages.composeapp.generated.resources.more_horiz
import sunboundpages.composeapp.generated.resources.search

@Composable
fun FinishedBooks(
    modifier: Modifier = Modifier,
    component: FinishedBooksComponent
) {

    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant
    val searchQuery = component.searchQuery.collectAsState()
    val filteredBooks = component.filteredBooks.collectAsState()

    Column(
        modifier = modifier
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
                colorFilter = ColorFilter.tint(iconColor)
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
                filteredBooks.value.forEach { book ->
                    FinishedBookItem(title = book.title, description = book.description)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun FinishedBookItem(
    title: String,
    description: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.library),
            contentDescription = "Book cover",
            modifier = Modifier
                .height(120.dp)
                .width(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    }
}
