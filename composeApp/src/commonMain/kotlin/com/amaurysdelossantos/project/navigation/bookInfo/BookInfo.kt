package com.amaurysdelossantos.project.navigation.bookInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import sunboundpages.composeapp.generated.resources.Res
import sunboundpages.composeapp.generated.resources.chevron_right
import sunboundpages.composeapp.generated.resources.library

@Composable
fun BookInfo(
    modifier: Modifier = Modifier,
    component: BookInfoComponent
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Book Header Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.library),
                contentDescription = "Book cover",
                modifier = Modifier
                    .size(width = 120.dp, height = 180.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Book Name",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "This is a short description of the book that should ideally wrap to two lines max.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())
                    ,horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(100) { i ->
                        Text(
                            text = "Tag $i",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    shape = RoundedCornerShape( 0.dp , 0.dp , 10.dp, 0.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chapters List Section
        Surface(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(top = 8.dp).verticalScroll(rememberScrollState())) {
                repeat(100) { index ->
                    ChapterItem(index = index, chapterName = "Chapter ${index + 1}")
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun ChapterItem(index: Int, chapterName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = chapterName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Subtitle or details",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Image(
            painter = painterResource(Res.drawable.chevron_right),
            contentDescription = "Go to chapter",
            modifier = Modifier.size(24.dp)
        )
    }
}
