package com.amaurysdelossantos.project.navigation.readingNow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amaurysdelossantos.project.customComposables.BookItem
import org.jetbrains.compose.resources.painterResource
import sunboundpages.composeapp.generated.resources.Res
import sunboundpages.composeapp.generated.resources.sample_book_cover

@Composable
fun ReadingNow(
    component: ReadingNowComponent,
    innerPadding: PaddingValues = PaddingValues()
) {
    val scrollState = rememberScrollState()
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val reading = component.currentlyReading.collectAsState(initial = emptyList())
    val resumed = component.recentlyOpened.collectAsState(initial = emptyList())
    val finished = component.finishedBooks.collectAsState(initial = emptyList())

    val isReadingEmpty = reading.value.isEmpty()
    val isResumedEmpty = resumed.value.isEmpty()
    val isFinishedEmpty = finished.value.isEmpty()

    val showPlaceholder = isReadingEmpty && isResumedEmpty && isFinishedEmpty

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        if (reading.value.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)
            ) {
                Text(
                    "Reading",
                    style = typography.headlineSmall,
                    color = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    reading.value.forEach {
                        BookItem(
                            it,
                            onClick = { component.onEvent(ReadingNowEvent.ClickBook(it.id.toString())) }
                        )
                    }
                }
            }
        }

        if (resumed.value.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(16.dp)
            ) {
                Text(
                    "Pick Up Where You Left Off",
                    style = typography.titleMedium,
                    color = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    resumed.value.forEach {
                        BookItem(
                            it,
                            onClick = { component.onEvent(ReadingNowEvent.ClickBook(it.id.toString())) }
                        )
                    }
                }
            }
        }

        if (finished.value.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Finished",
                        style = typography.titleMedium,
                        color = colorScheme.onSurface
                    )

                    Text(
                        "See All",
                        color = colorScheme.primary,
                        style = typography.labelLarge,
                        modifier = Modifier
                            .clickable {
                                component.onEvent(ReadingNowEvent.SeeAllFinished)
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    finished.value.forEach {
                        BookItem(
                            it,
                            onClick = { component.onEvent(ReadingNowEvent.ClickBook(it.id.toString())) }
                        )
                    }
                }
            }
        }

        if (showPlaceholder) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(Res.drawable.sample_book_cover),
                    contentDescription = "No books placeholder",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "No books to show yet.",
                    style = typography.bodyLarge,
                    color = colorScheme.onSurface
                )
            }
        }
    }
}

