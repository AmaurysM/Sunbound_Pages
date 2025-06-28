package com.amaurysdelossantos.project.customComposables


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.navigation.RootComponent.Configuration
import com.amaurysdelossantos.project.util.NavigationHolder
import com.arkivanov.decompose.router.stack.bringToFront
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun BookItem(
    book: Book,
    size: Dp = 150.dp,
    onClick: () -> Unit = {}
) {
    val width = size
    val height = size * 1.5f

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.shapes.medium
            )
            .clickable { NavigationHolder.navigation.bringToFront(Configuration.BookView(book.id.toString())) }
    ) {
        if (!book.coverImageUrl.isNullOrEmpty()) {
            val painterResource = asyncPainterResource(book.coverImageUrl!!)

            KamelImage(
                resource = painterResource,
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                onFailure = {
                    // Optional fallback if image fails
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Load Failed",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                onLoading = {
                    // Optional loading UI
                }
            )
        } else {
            // Placeholder when no cover image is available
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Cover",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        // Title overlay at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(
                    Color.Black.copy(alpha = 0.7f),
                    MaterialTheme.shapes.small
                )
                .padding(8.dp)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}