package com.amaurysdelossantos.project.navigation.bookView.ebook.readingEBook

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amaurysdelossantos.project.database.PreviewBookDao
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview()
@Composable
fun ReadingEBookViewPreview() {
    val component = ReadingEBookComponent(
        DefaultComponentContext(lifecycle = LifecycleRegistry()),
        bookId = "preview-book-1",
        bookDao = PreviewBookDao()
    )
    ReadingEBookView(component, PaddingValues(0.dp))
}

@Composable
fun ReadingEBookView(
    component: ReadingEBookComponent,
    innerPadding: PaddingValues,
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
}