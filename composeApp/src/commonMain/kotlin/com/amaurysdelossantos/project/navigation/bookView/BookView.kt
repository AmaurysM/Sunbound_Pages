package com.amaurysdelossantos.project.navigation.bookView

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.amaurysdelossantos.project.util.getBookFormat
import com.amaurysdelossantos.project.util.toMediaType

@Composable
fun BookView(
    component: BookViewComponent,
    innerPadding: PaddingValues = PaddingValues(),
) {
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

    val format = getBookFormat(book.filePath)
    val mediaType = format?.toMediaType()

//    when (mediaType) {
//        MediaType.EBOOK -> navigation.bringToFront(Configuration.EBookView(book.id))
//
//        MediaType.COMIC -> ComicView(book, component, innerPadding)
//        MediaType.AUDIOBOOK -> AudioBookView(book, component, innerPadding)
//        else -> navigation.bringToFront(Configuration.EBookView(book.id))
//    }
}


//@Composable
//fun BookInfo(
//    component: BookInfoComponent,
//    innerPadding: PaddingValues = PaddingValues(),
//    ) {
//
//    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant
//
//    val colorScheme = MaterialTheme.colorScheme
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//    ) {
//
//        Box(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = innerPadding.calculateTopPadding())
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp, vertical = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Image(
//                        painter = painterResource(Res.drawable.chevron_left),
//                        contentDescription = "Back button",
//                        colorFilter = ColorFilter.tint(iconColor),
//                        modifier = Modifier.clickable {
//                            component.onEvent(BookInfoEvent.BackClicked)
//                        }
//                    )
//
//                    Image(
//                        painter = painterResource(Res.drawable.more_horiz),
//                        contentDescription = "Show More Button",
//                        colorFilter = ColorFilter.tint(iconColor)
//                    )
//                }
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Image(
//                        painter = painterResource(Res.drawable.library),
//                        contentDescription = "Book cover",
//                        modifier = Modifier
//                            .size(width = 120.dp, height = 180.dp)
//                            .clip(RoundedCornerShape(6.dp))
//                            .background(MaterialTheme.colorScheme.onPrimary)
//                    )
//
//                    Spacer(modifier = Modifier.width(16.dp))
//
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(8.dp),
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text(
//                            text = "Book Name",
//                            style = MaterialTheme.typography.headlineSmall,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                        Text(
//                            text = "This is a short description of the book that should ideally wrap to two lines max.",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onPrimaryContainer
//                        )
//                        Row(
//                            modifier = Modifier
//                                .horizontalScroll(rememberScrollState()),
//                            horizontalArrangement = Arrangement.spacedBy(6.dp)
//                        ) {
//                            repeat(100) { i ->
//                                Text(
//                                    text = "Tag $i",
//                                    style = MaterialTheme.typography.labelSmall,
//                                    modifier = Modifier
//                                        .background(
//                                            color = MaterialTheme.colorScheme.onPrimary,
//                                            shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 0.dp)
//                                        )
//                                        .padding(horizontal = 8.dp, vertical = 4.dp)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // Chapters List Section
//        Surface(
//            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
//            tonalElevation = 1.dp,
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(top = 8.dp)
//                    .verticalScroll(rememberScrollState())
//            ) {
//                repeat(100) { index ->
//                    ChapterItem(index = index, chapterName = "Chapter ${index + 1}")
//                    Divider()
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//private fun ChapterItem(index: Int, chapterName: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 14.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Column {
//            Text(
//                text = chapterName,
//                style = MaterialTheme.typography.bodyLarge,
//                fontWeight = FontWeight.Medium
//            )
//            Text(
//                text = "Subtitle or details",
//                style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        }
//
//        Image(
//            painter = painterResource(Res.drawable.chevron_right),
//            contentDescription = "Go to chapter",
//            modifier = Modifier.size(24.dp)
//        )
//    }
//}
