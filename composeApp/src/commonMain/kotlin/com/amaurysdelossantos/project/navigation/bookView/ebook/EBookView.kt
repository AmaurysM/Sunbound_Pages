package com.amaurysdelossantos.project.navigation.bookView.ebook

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amaurysdelossantos.project.customComposables.BookItem
import com.amaurysdelossantos.project.database.PreviewBookDao
import com.amaurysdelossantos.project.navigation.bookView.DetailRow
import com.amaurysdelossantos.project.navigation.bookView.InfoCard
import com.amaurysdelossantos.project.navigation.bookView.StatItem
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.round


@Preview()
@Composable
fun EBookPreview() {
    val component = EBookViewComponent(
        DefaultComponentContext(lifecycle = LifecycleRegistry()),
        bookId = "preview-book-1",
        bookDao = PreviewBookDao()
    )
    EBookView(component, PaddingValues(0.dp))
}

@Composable
fun EBookView(
    component: EBookViewComponent,
    innerPadding: PaddingValues = PaddingValues(0.dp),
) {

    val scrollState = rememberScrollState()
    val book = component.book.collectAsState().value

    if (book == null) {
        Text(
            text = "Loading...",
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        // Header Card
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
                    size = 150.dp
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
                            text = book.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.primary)
                            .clickable {
                                component.onEvent(EBookEvent.StartBook)
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(30.dp)

                        )
                    }
                }
            }
        }

        // Book Details Cards
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            // Author Card
            book.author?.let { author ->
                item {
                    InfoCard(
                        icon = Icons.Default.Person,
                        title = "Author",
                        content = author,
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    )
                }
            }

            // Reading Stats Card
            item {
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

            // Description Card
            book.description?.let { description ->
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

            // Additional Info
            item {
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

                        book.genre?.let { genre ->
                            DetailRow(label = "Genre", value = genre)
                        }
                        book.language?.let { language ->
                            DetailRow(label = "Language", value = language)
                        }
                        book.publicationYear?.let { year ->
                            DetailRow(label = "Publication Year", value = year.toString())
                        }
                        book.publisher?.let { publisher ->
                            DetailRow(label = "Publisher", value = publisher)
                        }
                    }
                }
            }
        }
    }
}