package com.amaurysdelossantos.project.navigation

import com.amaurysdelossantos.project.database.BookDatabase
import com.amaurysdelossantos.project.navigation.RootComponent.Child.BookInfo
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Downloads
import com.amaurysdelossantos.project.navigation.RootComponent.Child.FinishedBooks
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Library
import com.amaurysdelossantos.project.navigation.RootComponent.Child.OnMyDevice
import com.amaurysdelossantos.project.navigation.RootComponent.Child.ReadingNow
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Search
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Settings
import com.amaurysdelossantos.project.navigation.bookInfo.BookInfoComponent
import com.amaurysdelossantos.project.navigation.downloads.DownloadsComponent
import com.amaurysdelossantos.project.navigation.finishedbooks.FinishedBooksComponent
import com.amaurysdelossantos.project.navigation.library.LibraryComponent
import com.amaurysdelossantos.project.navigation.onMyDevice.OnMyDeviceComponent
import com.amaurysdelossantos.project.navigation.readingNow.ReadingNowComponent
import com.amaurysdelossantos.project.navigation.search.SearchComponent
import com.amaurysdelossantos.project.navigation.settings.SettingsComponent
import com.amaurysdelossantos.project.util.NavigationHolder.navigation
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, KoinComponent {

    //    private val navigation = StackNavigation<Configuration>()
    val childStack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = Configuration.ReadingNow,
            handleBackButton = true,
            childFactory = ::createChild
        )

    private val bookDao = inject<BookDatabase>().value.bookDao()


    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when (config) {
            Configuration.ReadingNow -> ReadingNow(
                ReadingNowComponent(
                    componentContext = context,
                    onBookClicked = { id ->
                        navigation.bringToFront(Configuration.BookInfo(id))
                    },
                    onSeeMore = {
                        navigation.bringToFront(Configuration.FinishedBooks)
                    }
                )
            )

            Configuration.Library -> Library(
                LibraryComponent(
                    context,
                    onMyDeviceButton = {
                        navigation.bringToFront(Configuration.OnMyDevice)
                    }
                )
            )

            Configuration.Search -> Search(
                SearchComponent(context)
            )

            Configuration.Downloads -> Downloads(
                DownloadsComponent(context)
            )

            Configuration.Settings -> Settings(
                SettingsComponent(context)
            )

            is Configuration.BookInfo -> BookInfo(
                BookInfoComponent(
                    context,
                    config.bookId,
                    onBack = {
                        navigation.pop()
                    }
                ),
            )

            Configuration.FinishedBooks -> FinishedBooks(
                FinishedBooksComponent(
                    context,
                    onBack = {
                        navigation.pop()
                    },
                    onBookClicked = { id ->
                        navigation.bringToFront(Configuration.BookInfo(id))
                    },
                )
            )

            Configuration.OnMyDevice -> {
                OnMyDevice(
                    OnMyDeviceComponent(
                        context,
                        onBack = {
                            navigation.pop()
                        },
                        bookDao
                    )
                )
            }
        }
    }

    sealed class Child {
        data class ReadingNow(val component: ReadingNowComponent) : Child()

        data class Library(val component: LibraryComponent) : Child()

        data class Downloads(val component: DownloadsComponent) : Child()

        data class Search(val component: SearchComponent) : Child()

        data class Settings(val component: SettingsComponent) : Child()

        data class BookInfo(val component: BookInfoComponent) : Child()

        data class FinishedBooks(val component: FinishedBooksComponent) : Child()

        data class OnMyDevice(val component: OnMyDeviceComponent) : Child()
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        data object ReadingNow : Configuration

        @Serializable
        data object Library : Configuration

        @Serializable
        data object Downloads : Configuration

        @Serializable
        data object Search : Configuration

        @Serializable
        data object Settings : Configuration

        @Serializable
        data class BookInfo(val bookId: String) : Configuration
        companion object

        @Serializable
        data object FinishedBooks : Configuration

        @Serializable
        data object OnMyDevice : Configuration
    }
}

