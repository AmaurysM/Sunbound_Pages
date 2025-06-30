package com.amaurysdelossantos.project.navigation.bookView.ebook.readingEBook

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.database.enums.BookFormat
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.util.file_reader.readTextFile
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

// Enhanced theme data class
data class ReadingTheme(
    val backgroundColor: Color,
    val textColor: Color,
    val surfaceColor: Color,
    val primaryColor: Color,
    val name: String
) {
    companion object {
        val Light = ReadingTheme(
            backgroundColor = Color(0xFFFFFBF0),
            textColor = Color(0xFF2C2C2E),
            surfaceColor = Color.White,
            primaryColor = Color(0xFF007AFF),
            name = "Light"
        )

        val Dark = ReadingTheme(
            backgroundColor = Color(0xFF1C1C1E),
            textColor = Color(0xFFE5E5E7),
            surfaceColor = Color(0xFF2C2C2E),
            primaryColor = Color(0xFF32D74B),
            name = "Dark"
        )

        val Sepia = ReadingTheme(
            backgroundColor = Color(0xFFF4F1E8),
            textColor = Color(0xFF5C4B37),
            surfaceColor = Color(0xFFEDE7D3),
            primaryColor = Color(0xFF8B7355),
            name = "Sepia"
        )

        val Night = ReadingTheme(
            backgroundColor = Color(0xFF000000),
            textColor = Color(0xFF999999),
            surfaceColor = Color(0xFF1A1A1A),
            primaryColor = Color(0xFF666666),
            name = "Night"
        )
    }
}

// Enhanced reading settings
data class ReadingSettings(
    val fontSize: TextUnit = 16.sp,
    val lineSpacing: Float = 1.5f,
    val theme: ReadingTheme = ReadingTheme.Light,
    val paragraphSpacing: Float = 1.2f,
    val marginHorizontal: Int = 24,
    val marginVertical: Int = 16,
    val justifyText: Boolean = true,
    val autoScroll: Boolean = false,
    val autoScrollSpeed: Float = 1.0f
)

class ReadingEBookComponent(
    componentContext: ComponentContext,
    private val bookId: String,
    private val bookDao: BookDao,
    private val onBack: () -> Unit = {}
) : ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Book state
    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    private val _bookContent = MutableStateFlow<String?>(null)
    val bookContent: StateFlow<String?> = _bookContent

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loadingError = MutableStateFlow<String?>(null)
    val loadingError: StateFlow<String?> = _loadingError

    // UI state
    var showSettings by mutableStateOf(false)
    var showSystemUI by mutableStateOf(true)
    var readingSettings by mutableStateOf(ReadingSettings())

    // Reading progress
    private val _readingProgress = MutableStateFlow(0f)
    val readingProgress: StateFlow<Float> = _readingProgress

    var currentScrollPosition by mutableIntStateOf(0)
    private var totalContentHeight by mutableIntStateOf(0)

    // Lazy list state
    val lazyListState = LazyListState()

    // Computed properties
    val currentTheme: ReadingTheme
        get() = readingSettings.theme

    val estimatedReadingTime: Int by derivedStateOf {
        val content = _bookContent.value
        if (content != null) {
            val wordCount = content.split("\\s+".toRegex()).size
            // Average reading speed: 200-250 words per minute
            (wordCount / 225f).toInt()
        } else 0
    }

    init {
        loadBook(bookId)
        setupScrollTracking()
    }

    private fun setupScrollTracking() {
        coroutineScope.launch {
            snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
                .distinctUntilChanged()
                .collect { offset ->
                    currentScrollPosition = offset
                    updateReadingProgress()
                }
        }
    }

    private fun updateReadingProgress() {
        val progress = if (totalContentHeight > 0) {
            (currentScrollPosition.toFloat() / totalContentHeight).coerceIn(0f, 1f)
        } else 0f

        _readingProgress.value = progress

        // Save progress periodically
        _book.value?.let { book ->
            coroutineScope.launch {
                Logger.d { "Reading progress: ${(progress * 100).toInt()}% for book: ${book.title}" }
                // Here you could save to database
            }
        }
    }

    private fun loadBook(bookId: String) {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                _loadingError.value = null

                val loadedBook = bookDao.getBookById(bookId)
                _book.value = loadedBook

                if (loadedBook != null) {
                    loadBookContent(loadedBook)
                } else {
                    _loadingError.value = "Book not found"
                    Logger.e { "Book not found with ID: $bookId" }
                }
            } catch (exception: Exception) {
                _loadingError.value = "Failed to load book: ${exception}"
                Logger.e(exception) { "Error loading book" }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadBookContent(book: Book) {
        try {
            when (book.format) {
                BookFormat.TXT -> {
                    if (book.filePath.isBlank()) {
                        _loadingError.value = "Invalid file path"
                        return
                    }

                    val content = readTextFile(book.filePath)
                    if (content != null) {
                        // Process content for better reading experience
                        val processedContent = processTextContent(content)
                        _bookContent.value = processedContent
                        Logger.d { "Successfully loaded TXT content (${content.length} chars)" }
                    } else {
                        _loadingError.value = "Failed to read file"
                    }
                }

                BookFormat.PDF -> {
                    _bookContent.value = "PDF format support is coming soon.\n\nWe're working on adding PDF reading capabilities to provide you with the best reading experience."
                }

                BookFormat.EPUB -> {
                    _bookContent.value = "EPUB format support is coming soon.\n\nEPUB support will include proper formatting, images, and interactive elements."
                }

                else -> {
                    _bookContent.value = "Format ${book.format} is not yet supported.\n\nSupported formats: TXT\nComing soon: PDF, EPUB"
                }
            }
        } catch (exception: Exception) {
            _loadingError.value = "Error reading file: ${exception}"
            Logger.e(exception) { "Error loading book content" }
        }
    }

    private fun processTextContent(content: String): String {
        return content
            .replace("\r\n", "\n")
            .replace("\r", "\n")
            .replace(Regex("\n{3,}"), "\n\n") // Limit consecutive newlines
            .trim()
    }

    fun onEvent(event: ReadingEBookEvent) {
        when (event) {
            ReadingEBookEvent.OnBackPressed -> onBack()
            ReadingEBookEvent.OnSettingsClicked -> showSettings = !showSettings
            ReadingEBookEvent.OnFontSizeChanged -> {
                // Font size changes are handled directly in the UI
            }
            ReadingEBookEvent.OnDarkModeChanged -> {
                // Theme changes are handled directly in the UI
            }
            ReadingEBookEvent.OnLineSpacingChanged -> {
                // Line spacing changes are handled directly in the UI
            }
            ReadingEBookEvent.OnCloseSettings -> showSettings = false
        }
    }

    fun updateFontSize(newSize: TextUnit) {
        readingSettings = readingSettings.copy(fontSize = newSize)
    }

    fun updateLineSpacing(newSpacing: Float) {
        readingSettings = readingSettings.copy(lineSpacing = newSpacing)
    }

    fun updateTheme(newTheme: ReadingTheme) {
        readingSettings = readingSettings.copy(theme = newTheme)
    }

    fun updateMargins(horizontal: Int, vertical: Int) {
        readingSettings = readingSettings.copy(
            marginHorizontal = horizontal,
            marginVertical = vertical
        )
    }

    fun toggleSystemUI() {
        showSystemUI = !showSystemUI
    }

    fun retryLoading() {
        loadBook(bookId)
    }

    fun jumpToProgress(progress: Float) {
        coroutineScope.launch {
            val targetPosition = (totalContentHeight * progress).toInt()
            lazyListState.animateScrollToItem(0, targetPosition)
        }
    }

    fun saveBookmark() {
        coroutineScope.launch {
            val progress = _readingProgress.value
            Logger.d { "Saving bookmark at ${(progress * 100).toInt()}%" }
            // Here you could save bookmark to database
        }
    }

    // Auto-scroll functionality
    fun toggleAutoScroll() {
        readingSettings = readingSettings.copy(autoScroll = !readingSettings.autoScroll)

        if (readingSettings.autoScroll) {
            startAutoScroll()
        }
    }

    private fun startAutoScroll() {
        coroutineScope.launch {
            while (readingSettings.autoScroll) {
                kotlinx.coroutines.delay((1000 / readingSettings.autoScrollSpeed).toLong())
                if (lazyListState.canScrollForward) {
                    lazyListState.animateScrollBy(1f)
                } else {
                    // Reached end, stop auto-scroll
                    readingSettings = readingSettings.copy(autoScroll = false)
                }
            }
        }
    }

    fun updateAutoScrollSpeed(speed: Float) {
        readingSettings = readingSettings.copy(autoScrollSpeed = speed)
    }
}