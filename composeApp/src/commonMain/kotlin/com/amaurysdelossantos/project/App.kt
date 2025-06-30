package com.amaurysdelossantos.project

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.amaurysdelossantos.project.customComposables.BottomBar
import com.amaurysdelossantos.project.customComposables.BottomNavItem
import com.amaurysdelossantos.project.navigation.RootComponent
import com.amaurysdelossantos.project.navigation.RootComponent.Configuration
import com.amaurysdelossantos.project.navigation.bookView.comic.ComicView
import com.amaurysdelossantos.project.navigation.bookView.ebook.EBookView
import com.amaurysdelossantos.project.navigation.bookView.ebook.readingEBook.ReadingEBookView
import com.amaurysdelossantos.project.navigation.downloads.Downloads
import com.amaurysdelossantos.project.navigation.finishedbooks.FinishedBooks
import com.amaurysdelossantos.project.navigation.library.Library
import com.amaurysdelossantos.project.navigation.onMyDevice.OnMyDevice
import com.amaurysdelossantos.project.navigation.reading.ReadingView
import com.amaurysdelossantos.project.navigation.search.Search
import com.amaurysdelossantos.project.navigation.settings.Settings
import com.amaurysdelossantos.project.util.resolveAnimation
import com.arkivanov.decompose.FaultyDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalAnimationApi::class, FaultyDecomposeApi::class)
@Composable
@Preview
fun App(root: RootComponent) {
    MaterialTheme {
        val childStack by root.childStack.subscribeAsState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomBar(
                    selectedDestination = childStack.active.configuration as? RootComponent.Configuration,
                    onNavigationSelected = {
                        when (it) {
                            BottomNavItem.Reading -> root.navigateTo(Configuration.Reading)
                            BottomNavItem.Library -> root.navigateTo(Configuration.Library)
                            BottomNavItem.Downloads -> root.navigateTo(Configuration.Downloads)
                            BottomNavItem.Search -> root.navigateTo(Configuration.Search)
                            BottomNavItem.Settings -> root.navigateTo(Configuration.Settings)
                        }
                    }
                )
            }
        ) { innerPadding ->
            val fromConfig = root.previousConfiguration
            val toConfig = childStack.active.configuration

            Children(
                stack = childStack,
                modifier = Modifier,
                animation = stackAnimation { child, otherChild, direction ->
                    resolveAnimation(
                        from = root.previousConfiguration,
                        to = child.configuration as Configuration,
                        isForward = root.lastDirection == RootComponent.NavigationDirection.Forward
                    )
                }
            ) { child ->
                when (val instance = child.instance) {
//                    is RootComponent.Child.BookView -> BookView(
//                        component = instance.component,
//                        innerPadding = innerPadding,
//                    )

                    is RootComponent.Child.Downloads -> Downloads(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )

                    is RootComponent.Child.Library -> Library(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )

                    is RootComponent.Child.ReadingNow -> ReadingView(
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

                    is RootComponent.Child.ComicView -> ComicView(
                        component = instance.component,
                        innerPadding = innerPadding,
                    )
                }
            }
        }
    }
}