package com.amaurysdelossantos.project.navigation

import com.amaurysdelossantos.project.database.BookDatabase
import com.amaurysdelossantos.project.navigation.RootComponent.Child.BookView
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Downloads
import com.amaurysdelossantos.project.navigation.RootComponent.Child.EBookView
import com.amaurysdelossantos.project.navigation.RootComponent.Child.FinishedBooks
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Library
import com.amaurysdelossantos.project.navigation.RootComponent.Child.OnMyDevice
import com.amaurysdelossantos.project.navigation.RootComponent.Child.ReadingEBookView
import com.amaurysdelossantos.project.navigation.RootComponent.Child.ReadingNow
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Search
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Settings
import com.amaurysdelossantos.project.navigation.bookView.BookViewComponent
import com.amaurysdelossantos.project.navigation.bookView.ebook.EBookViewComponent
import com.amaurysdelossantos.project.navigation.bookView.ebook.readingEBook.ReadingEBookComponent
import com.amaurysdelossantos.project.navigation.downloads.DownloadsComponent
import com.amaurysdelossantos.project.navigation.finishedbooks.FinishedBooksComponent
import com.amaurysdelossantos.project.navigation.library.LibraryComponent
import com.amaurysdelossantos.project.navigation.onMyDevice.OnMyDeviceComponent
import com.amaurysdelossantos.project.navigation.reading.ReadingComponent
import com.amaurysdelossantos.project.navigation.search.SearchComponent
import com.amaurysdelossantos.project.navigation.settings.SettingsComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RootComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<Configuration>
) : ComponentContext by componentContext, KoinComponent {

    private val backCallback = BackCallback(isEnabled = true) {
        handleBackPressed()
    }

    init {
        // Register the back callback to intercept system back events
        backHandler.register(backCallback)
    }

    val childStack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = Configuration.Reading,
            handleBackButton = false, // We handle back events manually now
            childFactory = ::createChild
        )

    var lastConfiguration: Configuration? = null

    enum class NavigationDirection { Forward, Backward }

    var lastDirection: NavigationDirection = NavigationDirection.Forward
        private set

    fun navigateTo(configuration: Configuration) {
        lastConfiguration = childStack.active.configuration as Configuration?
        lastDirection = NavigationDirection.Forward
        navigation.bringToFront(configuration)
    }

    fun goBack() {
        lastConfiguration = childStack.active.configuration as Configuration?
        lastDirection = NavigationDirection.Backward
        navigation.pop()
    }

    private fun handleBackPressed() {
        // Check if we can go back
        if (childStack.value.items.size > 1) {
            // Set direction to backward for consistent animation
            lastConfiguration = childStack.active.configuration as Configuration?
            lastDirection = NavigationDirection.Backward
            navigation.pop()
        }

    }

    // Helper method to check if back navigation is possible
    fun canGoBack(): Boolean = childStack.value.items.size > 1

    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {

        val result = when (config) {
            Configuration.Reading -> ReadingNow(
                ReadingComponent(
                    componentContext = context,
                    onBookClicked = { id ->
                        navigateTo(Configuration.ReadingEBookView(id))
                    },
                    onSeeMore = {
                        navigateTo(Configuration.FinishedBooks)
                    },
                    bookDao = inject<BookDatabase>().value.bookDao()
                )
            )

            Configuration.Library -> Library(
                LibraryComponent(
                    context,
                    onMyDeviceButton = {
                        navigateTo(Configuration.OnMyDevice)
                    },
                    bookDao = inject<BookDatabase>().value.bookDao()
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

            is Configuration.BookView -> BookView(
                BookViewComponent(
                    context,
                    config.bookId,
                    onBack = {
                        goBack()
                    },
                    bookDao = inject<BookDatabase>().value.bookDao(),
                ),
            )

            Configuration.FinishedBooks -> FinishedBooks(
                FinishedBooksComponent(
                    context,
                    inject<BookDatabase>().value.bookDao(),
                    onBack = {
                        goBack()
                    },
                    onBookClicked = { id ->
                        navigateTo(Configuration.BookView(id))
                    },
                )
            )

            Configuration.OnMyDevice -> {
                OnMyDevice(
                    OnMyDeviceComponent(
                        context,
                        onBack = {
                            goBack()
                        },
                        bookDao = inject<BookDatabase>().value.bookDao(),
                        viewBook = { id ->
                            navigateTo(Configuration.BookView(id))
                        }
                    )
                )
            }

            is Configuration.ReadingEBookView -> {
                ReadingEBookView(
                    ReadingEBookComponent(
                        context,
                        config.bookId,
                        inject<BookDatabase>().value.bookDao()
                    )
                )
            }

            is Configuration.EBookView -> {
                EBookView(
                    EBookViewComponent(
                        context,
                        config.bookId,
                        inject<BookDatabase>().value.bookDao()
                    )
                )
            }
        }

        return result
    }

    sealed class Child {
        data class ReadingNow(val component: ReadingComponent) : Child()
        data class Library(val component: LibraryComponent) : Child()
        data class Downloads(val component: DownloadsComponent) : Child()
        data class Search(val component: SearchComponent) : Child()
        data class Settings(val component: SettingsComponent) : Child()
        data class BookView(val component: BookViewComponent) : Child()
        data class FinishedBooks(val component: FinishedBooksComponent) : Child()
        data class OnMyDevice(val component: OnMyDeviceComponent) : Child()
        data class ReadingEBookView(val component: ReadingEBookComponent) : Child()
        data class EBookView(val component: EBookViewComponent) : Child()
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        data object Reading : Configuration

        @Serializable
        data object Library : Configuration

        @Serializable
        data object Downloads : Configuration

        @Serializable
        data object Search : Configuration

        @Serializable
        data object Settings : Configuration

        @Serializable
        data class BookView(val bookId: String) : Configuration

        @Serializable
        data object FinishedBooks : Configuration

        @Serializable
        data object OnMyDevice : Configuration

        @Serializable
        data class ReadingEBookView(val bookId: String) : Configuration

        @Serializable
        data class EBookView(val bookId: String) : Configuration
    }
}