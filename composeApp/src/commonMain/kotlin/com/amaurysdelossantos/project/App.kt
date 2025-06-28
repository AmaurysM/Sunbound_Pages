package com.amaurysdelossantos.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.amaurysdelossantos.project.customComposables.BottomBar
import com.amaurysdelossantos.project.navigation.RootComponent
import com.amaurysdelossantos.project.navigation.bookView.BookView
import com.amaurysdelossantos.project.navigation.bookView.ebook.EBookView
import com.amaurysdelossantos.project.navigation.bookView.ebook.readingEBook.ReadingEBookView
import com.amaurysdelossantos.project.navigation.downloads.Downloads
import com.amaurysdelossantos.project.navigation.finishedbooks.FinishedBooks
import com.amaurysdelossantos.project.navigation.library.Library
import com.amaurysdelossantos.project.navigation.onMyDevice.OnMyDevice
import com.amaurysdelossantos.project.navigation.readingNow.ReadingNow
import com.amaurysdelossantos.project.navigation.search.Search
import com.amaurysdelossantos.project.navigation.settings.Settings
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(root: RootComponent) {
    MaterialTheme {
        val childStack by root.childStack.subscribeAsState()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomBar()
            }
        ) { innerPadding ->

            Children(
                stack = childStack,
                animation = stackAnimation(fade())
            ) { child ->
                when (val instance = child.instance) {
                    is RootComponent.Child.BookView -> BookView(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )

                    is RootComponent.Child.Downloads -> Downloads(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )

                    is RootComponent.Child.Library -> Library(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )

                    is RootComponent.Child.ReadingNow -> ReadingNow(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )

                    is RootComponent.Child.Search -> Search(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )

                    is RootComponent.Child.Settings -> Settings(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )

                    is RootComponent.Child.FinishedBooks -> FinishedBooks(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )

                    is RootComponent.Child.OnMyDevice -> OnMyDevice(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )

                    is RootComponent.Child.ReadingEBookView -> ReadingEBookView(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )

                    is RootComponent.Child.EBookView -> EBookView(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )
                }
            }
        }
    }
}