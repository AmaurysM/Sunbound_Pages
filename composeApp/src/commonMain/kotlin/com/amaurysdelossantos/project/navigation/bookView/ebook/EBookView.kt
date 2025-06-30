package com.amaurysdelossantos.project.navigation.bookView.ebook

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amaurysdelossantos.project.customComposables.BookItem
import com.amaurysdelossantos.project.database.PreviewBookDao
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.navigation.bookView.DetailRow
import com.amaurysdelossantos.project.navigation.bookView.InfoCard
import com.amaurysdelossantos.project.navigation.bookView.StatItem
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import sunboundpages.composeapp.generated.resources.Res
import sunboundpages.composeapp.generated.resources.chevron_left
import sunboundpages.composeapp.generated.resources.more_horiz
import kotlin.math.round

sealed class EBookUiState {
    object Loading : EBookUiState()
    data class Success(val book: Book) : EBookUiState()
    data class Error(val message: String) : EBookUiState()
    object Empty : EBookUiState()
}

@Preview()
@Composable
fun EBookPreview() {
    val component = EBookViewComponent(
        DefaultComponentContext(lifecycle = LifecycleRegistry()),
        bookId = "preview-book-1",
        bookDao = PreviewBookDao()
    )
    EBookView(component)
}

@Composable
fun EBookView(
    component: EBookViewComponent,
    innerPadding: PaddingValues = PaddingValues(0.dp),
) {
    val book = component.book.collectAsState().value

    val uiState = when {
        book == null -> EBookUiState.Loading
        else -> EBookUiState.Success(book)
    }

    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant

    Scaffold(
        modifier = Modifier.fillMaxSize()//.background(MaterialTheme.colorScheme.background).padding(innerPadding),
        , topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(top = innerPadding.calculateTopPadding(), start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(Res.drawable.chevron_left),
                    contentDescription = "Back button",
                    colorFilter = ColorFilter.tint(iconColor),
                    modifier = Modifier.clickable { component.onEvent(EBookEvent.NavigateBack) }
                )



                Image(
                    painter = painterResource(Res.drawable.more_horiz),
                    contentDescription = "Show More Button",
                    colorFilter = ColorFilter.tint(iconColor)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding() ,
                    bottom = innerPadding.calculateBottomPadding()
                )
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (uiState) {
                is EBookUiState.Loading -> LoadingState()
                is EBookUiState.Success -> BookContent(
                    book = uiState.book,
                    onStartReading = { component.onEvent(EBookEvent.StartBook) }
                )

                is EBookUiState.Error -> ErrorState(
                    message = uiState.message,
                    onRetry = { /* component.onEvent(EBookEvent.Retry) */ }
                )

                is EBookUiState.Empty -> EmptyState()
            }
        }
    }


}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Loading book details...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Unable to load book",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(onClick = onRetry) {
            Text("Try Again")
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "No book",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No book found",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The requested book could not be found in your library.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BookContent(
    book: Book,
    onStartReading: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header Card
        BookHeader(
            book = book,
            onStartReading = onStartReading
        )

        // Book Details Cards
        BookDetails(book = book)
    }
}

@Composable
private fun BookHeader(
    book: Book,
    onStartReading: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            BookItem(
                book = book,
                size = 150.dp,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(200.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "DIGITAL BOOK",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = book.title.ifEmpty { "Unknown Title" },
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Show author if available
                    book.author?.let { author ->
                        if (author.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "by $author",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .clickable { onStartReading() }
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Reading",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookDetails(book: Book) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        // Author Card - only if author exists and is not empty
        book.author?.let { author ->
            if (author.isNotEmpty()) {
                item {
                    InfoCard(
                        icon = Icons.Default.Person,
                        title = "Author",
                        content = author,
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    )
                }
            }
        }

        // Reading Stats Card - only show if we have at least one stat
        val hasStats = book.totalPages != null ||
                book.estimatedReadingTimeMinutes != null ||
                book.rating != null

        if (hasStats) {
            item {
                ReadingStatsCard(book = book)
            }
        }

        // Description Card - only if description exists and is not empty
        book.description?.let { description ->
            if (description.isNotEmpty()) {
                item {
                    InfoCard(
                        icon = Icons.Default.Description,
                        title = "Description",
                        content = description,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        isExpandable = true
                    )
                }
            }
        }

        // Additional Info - only show if we have at least one additional field
        val hasAdditionalInfo = book.genre?.isNotEmpty() == true ||
                book.language?.isNotEmpty() == true ||
                book.publicationYear != null ||
                book.publisher?.isNotEmpty() == true

        if (hasAdditionalInfo) {
            item {
                AdditionalInfoCard(book = book)
            }
        }

        // If no additional info is available, show a message
        if (!hasStats && book.description.isNullOrEmpty() && !hasAdditionalInfo) {
            item {
                EmptyInfoCard()
            }
        }
    }
}

@Composable
private fun ReadingStatsCard(book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = "Stats",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Reading Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                book.totalPages?.let { pages ->
                    StatItem(
                        icon = Icons.Default.Description,
                        value = pages.toString(),
                        label = "Pages"
                    )
                }

                book.estimatedReadingTimeMinutes?.let { minutes ->
                    val hours = minutes / 60
                    val remainingMinutes = minutes % 60
                    StatItem(
                        icon = Icons.Default.Schedule,
                        value = if (hours > 0) "${hours}h ${remainingMinutes}m" else "${minutes}m",
                        label = "Est. Time"
                    )
                }

                book.rating?.let { rating ->
                    StatItem(
                        icon = Icons.Default.Star,
                        value = "${((round(rating * 10) / 10) * 10)}/10",
                        label = "Rating"
                    )
                }
            }
        }
    }
}

@Composable
private fun AdditionalInfoCard(book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Additional Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            book.genre?.takeIf { it.isNotEmpty() }?.let { genre ->
                DetailRow(label = "Genre", value = genre)
            }
            book.language?.takeIf { it.isNotEmpty() }?.let { language ->
                DetailRow(label = "Language", value = language)
            }
            book.publicationYear?.let { year ->
                DetailRow(label = "Publication Year", value = year.toString())
            }
            book.publisher?.takeIf { it.isNotEmpty() }?.let { publisher ->
                DetailRow(label = "Publisher", value = publisher)
            }
        }
    }
}

@Composable
private fun EmptyInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "No additional info",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Limited Information Available",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Additional book details are not available for this title.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}