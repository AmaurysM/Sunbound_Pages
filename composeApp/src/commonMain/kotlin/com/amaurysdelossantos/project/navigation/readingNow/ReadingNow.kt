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

    Column(
        modifier = Modifier.padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {

        // READING /1//1/1/1//1!1/1!1/1/1/1/1/1
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                //.background(colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
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
                repeat(5) {
                    BookItem(
                        painter = painterResource(Res.drawable.sample_book_cover),
                        title = "Book Title",
                        size = 150.dp,
                        onClick = { component.onEvent(ReadingNowEvent.ClickBook("0")) }
                    )
                }
            }
        }

        // Pick Up Where You Left Off ??!??!?!?!??!?!?!??!

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                //.background(colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
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
                repeat(5) {
                    BookItem(
                        painter = painterResource(Res.drawable.sample_book_cover),
                        title = "",
                        size = 80.dp,
                        onClick = { component.onEvent(ReadingNowEvent.ClickBook("0")) }
                    )
                }
            }
        }

        // FINISHED BOOKS???????!!!??!?!?!??!?!?

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                //.background(colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
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
                repeat(5) {
                    BookItem(
                        painter = painterResource(Res.drawable.sample_book_cover),
                        title = "",
                        size = 80.dp,
                        onClick = { component.onEvent(ReadingNowEvent.ClickBook("0")) }
                    )
                }
            }
        }
    }
}
