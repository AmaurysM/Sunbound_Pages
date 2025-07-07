package com.amaurysdelossantos.project.navigation.bookView.ebook.readingEBook

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaurysdelossantos.project.database.PreviewBookDao
import com.amaurysdelossantos.project.database.enums.BookFormat
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.util.toMediaType
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.datetime.Clock
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ReadingEBookViewPreview() {
    val component = ReadingEBookComponent(
        DefaultComponentContext(lifecycle = LifecycleRegistry()),
        bookId = "preview-book-1",
        bookDao = PreviewBookDao()
    )
    ReadingEBookView(component, PaddingValues(0.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingEBookView(
    component: ReadingEBookComponent,
    innerPadding: PaddingValues,
) {
    val book by component.book.collectAsState()
    val content by component.bookContent.collectAsState()
    val isLoading by component.isLoading.collectAsState()
    val loadingError by component.loadingError.collectAsState()
    val readingProgress by component.readingProgress.collectAsState()

    val theme = component.currentTheme
    val settings = component.readingSettings

    // Auto-hide UI after tap
    var lastTapTime by remember { mutableStateOf(0L) }
    //val documentUri = remember { book?.filePath ?: "" }

    LaunchedEffect(lastTapTime) {
        if (lastTapTime > 0) {
            kotlinx.coroutines.delay(3000) // Hide after 3 seconds
            if (Clock.System.now().toEpochMilliseconds() - lastTapTime >= 3000) {
                component.showSystemUI = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        lastTapTime = Clock.System.now().toEpochMilliseconds()
                        component.toggleSystemUI()
                    }
                )
            }
    ) {
        when {
            isLoading -> {
                LoadingView(theme)
            }

            loadingError != null -> {
                ErrorView(
                    error = loadingError!!,
                    theme = theme,
                    onRetry = component::retryLoading
                )
            }

//            when(book?.format){
//                BookFormat.TXT -> true
//                BookFormat.PDF -> true
//                BookFormat.EPUB -> true
//                else -> false
//            }
//            content?.contains("support") == true -> {
//                UnsupportedFormatView(
//                    content = content!!,
//                    theme = theme,
//                    book = book
//                )
//            }

            else -> {



//                when(book?.format){
//                    BookFormat.TXT -> BookContentView(
//                        content = content,
//                        settings = settings,
//                        theme = theme,
//                        lazyListState = component.lazyListState,
//                        innerPadding = innerPadding
//                    )
//                    BookFormat.PDF -> DocumentView(
//                        documentUri = book!!.filePath,
//                        modifier = Modifier
//                            .height(100.dp)
//                            .padding(16.dp)
//                    )
//                    BookFormat.EPUB -> true
//                    else -> false
//                }
//                BookContentView(
//                    content = content,
//                    settings = settings,
//                    theme = theme,
//                    lazyListState = component.lazyListState,
//                    innerPadding = innerPadding
//                )
//                EBookContentView(
//                    filePath = book!!.filePath,
//                    format = book!!.format.toMediaType(),
//                )
            }
        }

        // Top App Bar (with animation)
        AnimatedVisibility(
            visible = component.showSystemUI,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(300)
            ),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = book?.title ?: "Reading",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (component.estimatedReadingTime > 0) {
                            Text(
                                text = "${component.estimatedReadingTime} min read",
                                fontSize = 12.sp,
                                color = theme.textColor.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { component.onEvent(ReadingEBookEvent.OnBackPressed) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = theme.textColor
                        )
                    }
                },
                actions = {
                    IconButton(onClick = component::saveBookmark) {
                        Icon(
                            imageVector = if (readingProgress > 0) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = theme.primaryColor
                        )
                    }


                    IconButton(onClick = { component.showSettings = !component.showSettings }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Reading Settings",
                            tint = theme.textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = theme.surfaceColor.copy(alpha = 0.95f),
                    titleContentColor = theme.textColor,
                    navigationIconContentColor = theme.textColor,
                    actionIconContentColor = theme.textColor
                )
            )
        }

        // Bottom progress bar (with animation)
        AnimatedVisibility(
            visible = component.showSystemUI && readingProgress > 0,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(300)
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ProgressBar(
                progress = readingProgress,
                theme = theme,
                onProgressChange = component::jumpToProgress
            )
        }

        // Reading settings panel
        AnimatedVisibility(
            visible = component.showSettings,
            enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(300)
            ),
            exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(300)
            )
        ) {
            ReadingSettingsPanel(
                settings = settings,
                theme = theme,
                onSettingsChange = { newSettings ->
                    component.readingSettings = newSettings
                },
                onClose = { component.showSettings = false }
            )
        }
    }
}

@Composable
private fun LoadingView(theme: ReadingTheme) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = theme.primaryColor,
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading your book...",
                color = theme.textColor,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ErrorView(
    error: String,
    theme: ReadingTheme,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = theme.surfaceColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚠️",
                    fontSize = 48.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Oops! Something went wrong",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.textColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    fontSize = 14.sp,
                    color = theme.textColor.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRetry,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = theme.primaryColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Try Again")
                }
            }
        }
    }
}

@Composable
private fun UnsupportedFormatView(
    content: String,
    theme: ReadingTheme,
    book: Book?
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = theme.surfaceColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📚",
                    fontSize = 48.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Format Not Supported Yet",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.textColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = content,
                    fontSize = 14.sp,
                    color = theme.textColor.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                if (book != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Book: ${book.title}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.textColor,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Format: ${book.format}",
                        fontSize = 14.sp,
                        color = theme.textColor.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun BookContentView(
    content: String?,
    settings: ReadingSettings,
    theme: ReadingTheme,
    lazyListState: androidx.compose.foundation.lazy.LazyListState,
    innerPadding: PaddingValues
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = settings.marginHorizontal.dp,
            vertical = settings.marginVertical.dp
        )
    ) {
        item {
            Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))
        }

        item {
            Text(
                text = content ?: "",
                fontSize = settings.fontSize,
                lineHeight = settings.fontSize * settings.lineSpacing,
                color = theme.textColor,
                textAlign = if (settings.justifyText) TextAlign.Justify else TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for system UI
        }
    }
}

@Composable
private fun ProgressBar(
    progress: Float,
    theme: ReadingTheme,
    onProgressChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = theme.surfaceColor.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = theme.textColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Reading Progress",
                    color = theme.textColor.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = progress,
                onValueChange = onProgressChange,
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.SliderDefaults.colors(
                    thumbColor = theme.primaryColor,
                    activeTrackColor = theme.primaryColor,
                    inactiveTrackColor = theme.primaryColor.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
private fun ReadingSettingsPanel(
    settings: ReadingSettings,
    theme: ReadingTheme,
    onSettingsChange: (ReadingSettings) -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onClose() }
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { }, // Prevent click through
            colors = CardDefaults.cardColors(
                containerColor = theme.surfaceColor
            ),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Reading Settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.textColor,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Theme selection
                Text(
                    text = "Theme",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = theme.textColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    val themes = listOf(
                        ReadingTheme.Light,
                        ReadingTheme.Dark,
                        ReadingTheme.Sepia,
                        ReadingTheme.Night
                    )

                    items(themes) { themeOption ->
                        ThemeCard(
                            theme = themeOption,
                            isSelected = settings.theme == themeOption,
                            onClick = {
                                onSettingsChange(settings.copy(theme = themeOption))
                            }
                        )
                    }
                }

                // Font size setting
                SettingRow(
                    icon = Icons.Default.FormatSize,
                    title = "Font Size",
                    theme = theme
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalButton(
                            onClick = {
                                if (settings.fontSize.value > 12f) {
                                    onSettingsChange(settings.copy(fontSize = (settings.fontSize.value - 2).sp))
                                }
                            },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = CircleShape
                        ) {
                            Text("A", fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "${settings.fontSize.value.toInt()}",
                            color = theme.textColor,
                            modifier = Modifier.widthIn(min = 24.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        FilledTonalButton(
                            onClick = {
                                if (settings.fontSize.value < 28f) {
                                    onSettingsChange(settings.copy(fontSize = (settings.fontSize.value + 2).sp))
                                }
                            },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = CircleShape
                        ) {
                            Text("A", fontSize = 16.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Line spacing setting
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Line Spacing",
                            color = theme.textColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${(settings.lineSpacing * 100).toInt()}%",
                            color = theme.textColor.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = settings.lineSpacing,
                        onValueChange = { newSpacing ->
                            onSettingsChange(settings.copy(lineSpacing = newSpacing))
                        },
                        valueRange = 1.0f..2.5f,
                        steps = 6,
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.SliderDefaults.colors(
                            thumbColor = theme.primaryColor,
                            activeTrackColor = theme.primaryColor,
                            inactiveTrackColor = theme.primaryColor.copy(alpha = 0.3f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Margin settings
                Column {
                    Text(
                        text = "Margins",
                        color = theme.textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                onSettingsChange(settings.copy(marginHorizontal = 16, marginVertical = 12))
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Narrow", fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(
                            onClick = {
                                onSettingsChange(settings.copy(marginHorizontal = 24, marginVertical = 16))
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Normal", fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(
                            onClick = {
                                onSettingsChange(settings.copy(marginHorizontal = 32, marginVertical = 20))
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Wide", fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Text justification toggle
                SettingRow(
                    icon = Icons.Default.Brightness4,
                    title = "Justify Text",
                    theme = theme
                ) {
                    Switch(
                        checked = settings.justifyText,
                        onCheckedChange = { isChecked ->
                            onSettingsChange(settings.copy(justifyText = isChecked))
                        },
                        colors = androidx.compose.material3.SwitchDefaults.colors(
                            checkedThumbColor = theme.primaryColor,
                            checkedTrackColor = theme.primaryColor.copy(alpha = 0.5f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Auto-scroll settings
                if (settings.autoScroll) {
                    Column {
                        Text(
                            text = "Auto-scroll Speed",
                            color = theme.textColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            value = settings.autoScrollSpeed,
                            onValueChange = { newSpeed ->
                                onSettingsChange(settings.copy(autoScrollSpeed = newSpeed))
                            },
                            valueRange = 0.5f..3.0f,
                            steps = 9,
                            modifier = Modifier.fillMaxWidth(),
                            colors = androidx.compose.material3.SliderDefaults.colors(
                                thumbColor = theme.primaryColor,
                                activeTrackColor = theme.primaryColor,
                                inactiveTrackColor = theme.primaryColor.copy(alpha = 0.3f)
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Slow",
                                color = theme.textColor.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Fast",
                                color = theme.textColor.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Close button
                FilledTonalButton(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.filledTonalButtonColors(
                        containerColor = theme.primaryColor.copy(alpha = 0.2f),
                        contentColor = theme.primaryColor
                    )
                ) {
                    Text(
                        text = "Done",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeCard(
    theme: ReadingTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(80.dp, 60.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) theme.primaryColor.copy(alpha = 0.2f) else Color.Transparent
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) theme.primaryColor else theme.textColor.copy(alpha = 0.3f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Theme preview
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(theme.backgroundColor)
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            theme.surfaceColor,
                            RoundedCornerShape(2.dp)
                        )
                )

                Spacer(modifier = Modifier.height(4.dp))

                repeat(3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (it == 2) 0.7f else 1f)
                            .height(2.dp)
                            .background(
                                theme.textColor.copy(alpha = 0.6f),
                                RoundedCornerShape(1.dp)
                            )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }

            // Theme name
            Text(
                text = theme.name,
                fontSize = 10.sp,
                color = theme.textColor,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(
                        theme.backgroundColor.copy(alpha = 0.8f),
                        RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    title: String,
    theme: ReadingTheme,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = theme.textColor.copy(alpha = 0.8f),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            color = theme.textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        content()
    }
}