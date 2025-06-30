package com.amaurysdelossantos.project.navigation.bookView

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
    }

}
