package com.amaurysdelossantos.project.navigation

import co.touchlab.kermit.Logger
import com.amaurysdelossantos.project.database.BookDatabase
import com.amaurysdelossantos.project.database.enums.MediaType
import com.amaurysdelossantos.project.navigation.RootComponent.Child.ComicView
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Downloads
import com.amaurysdelossantos.project.navigation.RootComponent.Child.EBookView
import com.amaurysdelossantos.project.navigation.RootComponent.Child.FinishedBooks
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Library
import com.amaurysdelossantos.project.navigation.RootComponent.Child.OnMyDevice
import com.amaurysdelossantos.project.navigation.RootComponent.Child.ReadingEBookView
import com.amaurysdelossantos.project.navigation.RootComponent.Child.ReadingNow
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Search
import com.amaurysdelossantos.project.navigation.RootComponent.Child.Settings
import com.amaurysdelossantos.project.navigation.bookView.comic.ComicInfoComponent
import com.amaurysdelossantos.project.navigation.bookView.ebook.EBookViewComponent
import com.amaurysdelossantos.project.navigation.bookView.ebook.readingEBook.ReadingEBookComponent
import com.amaurysdelossantos.project.navigation.downloads.DownloadsComponent
import com.amaurysdelossantos.project.navigation.finishedbooks.FinishedBooksComponent
import com.amaurysdelossantos.project.navigation.library.LibraryComponent
import com.amaurysdelossantos.project.navigation.onMyDevice.OnMyDeviceComponent
import com.amaurysdelossantos.project.navigation.reading.ReadingComponent
import com.amaurysdelossantos.project.navigation.search.SearchComponent
import com.amaurysdelossantos.project.navigation.settings.SettingsComponent
import com.amaurysdelossantos.project.util.getBookFormat
import com.amaurysdelossantos.project.util.toMediaType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    enum class NavigationDirection { Forward, Backward }

    var lastDirection: NavigationDirection = NavigationDirection.Forward
        private set

    private var _previousConfiguration: Configuration? = null
    val previousConfiguration: Configuration? get() = _previousConfiguration

    private var isNavigating = false

    fun navigateTo(configuration: Configuration) {
        if (isNavigating || childStack.active.configuration == configuration) return

        isNavigating = true
        _previousConfiguration = childStack.active.configuration as Configuration?
        lastDirection = NavigationDirection.Forward

        navigation.bringToFront(configuration)

        CoroutineScope(Dispatchers.Main).launch {
            kotlinx.coroutines.delay(300)
            isNavigating = false
        }
    }

    // Helper method to navigate to book view with proper media type detection
    fun navigateToBookView(bookId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val bookDao = inject<BookDatabase>().value.bookDao()
                val book = bookDao.getBookById(bookId)
                val format = getBookFormat(book?.filePath.orEmpty())
                val mediaType = format?.toMediaType()

                val destination = when (mediaType) {
                    MediaType.EBOOK,
                    MediaType.LIGHT_NOVEL,
                    MediaType.VISUAL_NOVEL -> Configuration.BookView.EBookView(bookId)

                    MediaType.COMIC,
                    MediaType.MANGA,
                    MediaType.MANHWA,
                    MediaType.MANHUA,
                    MediaType.WEBTOON -> Configuration.BookView.ComicView(bookId)

                    MediaType.AUDIOBOOK -> Configuration.BookView.EBookView.ReadingEBookView(bookId)
                    null -> Configuration.BookView.EBookView(bookId) // Default fallback
                }

                navigateTo(destination)
            } catch (e: Exception) {
                Logger.e(e) { "Error navigating to book view for bookId: $bookId" }
                // Fallback to EBookView if there's an error
                navigateTo(Configuration.BookView.EBookView(bookId))
            }
        }
    }

    fun goBack() {
        _previousConfiguration = childStack.active.configuration as Configuration?
        lastDirection = NavigationDirection.Backward
        navigation.pop()
    }

    fun replaceSelfWith(configuration: Configuration) {
        _previousConfiguration = childStack.active.configuration as Configuration?
        lastDirection = NavigationDirection.Forward
        navigation.replaceCurrent(configuration)
    }

    private fun handleBackPressed() {
        if (childStack.value.items.size > 1) {
            _previousConfiguration = childStack.active.configuration as Configuration?
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
                        navigateTo(Configuration.BookView.EBookView.ReadingEBookView(id))
                    },
                    onSeeMore = {
                        navigateTo(Configuration.Reading.FinishedBooks)
                    },
                    bookDao = inject<BookDatabase>().value.bookDao()
                )
            )

            Configuration.Library -> Library(
                LibraryComponent(
                    context,
                    onMyDeviceButton = {
                        navigateTo(Configuration.Downloads.OnMyDevice)
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

            // REMOVED: The problematic BookView configuration that was causing issues
            // Now we navigate directly to specific book view types

            Configuration.Reading.FinishedBooks -> FinishedBooks(
                FinishedBooksComponent(
                    context,
                    inject<BookDatabase>().value.bookDao(),
                    onBack = {
                        goBack()
                    },
                    onBookClicked = { id ->
                        // Use the new helper method instead of the problematic BookView
                        navigateToBookView(id)
                    },
                )
            )

            Configuration.Downloads.OnMyDevice -> {
                OnMyDevice(
                    OnMyDeviceComponent(
                        context,
                        onBack = {
                            goBack()
                        },
                        bookDao = inject<BookDatabase>().value.bookDao(),
                        viewBook = { id ->
                            // Use the new helper method instead of the problematic BookView
                            navigateToBookView(id)
                        }
                    )
                )
            }

            is Configuration.BookView.EBookView.ReadingEBookView -> {
                ReadingEBookView(
                    ReadingEBookComponent(
                        context,
                        config.bookId,
                        inject<BookDatabase>().value.bookDao()
                    )
                )
            }

            is Configuration.BookView.EBookView -> {
                EBookView(
                    EBookViewComponent(
                        context,
                        config.bookId,
                        inject<BookDatabase>().value.bookDao(),
                        readBook = { id ->
                            Logger.e { "Reading book with id: $id" }
                            navigateTo(Configuration.BookView.EBookView.ReadingEBookView(id))
                        }
                    )
                )
            }

            is Configuration.BookView.ComicView -> ComicView(
                ComicInfoComponent(
                    context,
                    config.bookId,
                    bookDao = inject<BookDatabase>().value.bookDao()
                )
            )
        }

        return result
    }

    sealed class Child {
        data class ReadingNow(val component: ReadingComponent) : Child()
        data class Library(val component: LibraryComponent) : Child()
        data class Downloads(val component: DownloadsComponent) : Child()
        data class Search(val component: SearchComponent) : Child()
        data class Settings(val component: SettingsComponent) : Child()
        data class FinishedBooks(val component: FinishedBooksComponent) : Child()
        data class OnMyDevice(val component: OnMyDeviceComponent) : Child()
        data class ReadingEBookView(val component: ReadingEBookComponent) : Child()
        data class EBookView(val component: EBookViewComponent) : Child()
        data class ComicView(val component: ComicInfoComponent) : Child()
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        data object Reading : Configuration {
            @Serializable
            data object FinishedBooks : Configuration
        }

        @Serializable
        data object Library : Configuration

        @Serializable
        data object Downloads : Configuration {
            @Serializable
            data object OnMyDevice : Configuration
        }

        @Serializable
        data object Search : Configuration

        @Serializable
        data object Settings : Configuration

        @Serializable
        sealed interface BookView : Configuration {

            @Serializable
            data class EBookView(val bookId: String) : BookView {

                @Serializable
                data class ReadingEBookView(val bookId: String) : BookView

            }

            @Serializable
            data class ComicView(val bookId: String) : BookView

        }
    }
}