package com.amaurysdelossantos.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.amaurysdelossantos.project.customComposables.bottom_bar.BottomBar
import com.amaurysdelossantos.project.customComposables.bottom_bar.BottomNavItem
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

    var isBottomBarVisible by remember { mutableStateOf(true) }

    MaterialTheme {
        val childStack by root.childStack.subscribeAsState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                AnimatedVisibility(
                    visible = isBottomBarVisible,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    BottomBar(
                        selectedDestination = childStack.active.configuration as? Configuration,
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

                    is RootComponent.Child.Downloads -> {
                        isBottomBarVisible = true

                        Downloads(
                            component = instance.component,
                            innerPadding = innerPadding,
                        )
                    }
                    is RootComponent.Child.Library -> {
                        isBottomBarVisible = true

                        Library(
                            component = instance.component,
                            innerPadding = innerPadding,

                        )
                    }

                    is RootComponent.Child.ReadingNow -> {
                        isBottomBarVisible = true

                        ReadingView(
                            component = instance.component,
                            innerPadding = innerPadding,

                        )
                    }

                    is RootComponent.Child.Search -> {
                        isBottomBarVisible = true

                        Search(
                            component = instance.component,
                            innerPadding = innerPadding,


                        )
                    }

                    is RootComponent.Child.Settings -> {
                        isBottomBarVisible = true

                        Settings(
                            component = instance.component,
                            innerPadding = innerPadding,


                        )
                    }

                    is RootComponent.Child.FinishedBooks -> {
                        isBottomBarVisible = true

                        FinishedBooks(
                            component = instance.component,
                            innerPadding = innerPadding,

                        )
                    }

                    is RootComponent.Child.OnMyDevice -> {
                        isBottomBarVisible = true

                        OnMyDevice(
                            component = instance.component,
                            innerPadding = innerPadding,

                        )
                    }

                    is RootComponent.Child.ReadingEBookView ->  {
                        isBottomBarVisible = false
                        ReadingEBookView(
                            component = instance.component,
                            innerPadding = innerPadding,
                        )
                    }

                    is RootComponent.Child.EBookView -> {
                        isBottomBarVisible = false
                        EBookView(
                            component = instance.component,
                            innerPadding = innerPadding,
                        )
                    }

                    is RootComponent.Child.ComicView -> {
                        isBottomBarVisible = false
                        ComicView(
                            component = instance.component,
                            innerPadding = innerPadding,
                        )
                    }
                }
            }
        }
    }
}