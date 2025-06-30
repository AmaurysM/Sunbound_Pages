package com.amaurysdelossantos.project.navigation.bookView.ebook.readingEBook

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaurysdelossantos.project.database.PreviewBookDao
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
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
    val lazyListState = rememberLazyListState()

    // Reading settings state
    var showSettings by remember { mutableStateOf(false) }
    var fontSize by remember { mutableStateOf(16.sp) }
    var isDarkMode by remember { mutableStateOf(false) }
    var lineSpacing by remember { mutableStateOf(1.5f) }

    // Theme colors based on mode
    val backgroundColor = if (isDarkMode) Color(0xFF1C1C1E) else Color(0xFFFFFBF0)
    val textColor = if (isDarkMode) Color(0xFFE5E5E7) else Color(0xFF2C2C2E)
    val surfaceColor = if (isDarkMode) Color(0xFF2C2C2E) else Color.White

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = book?.title ?: "Reading",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { component.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showSettings = !showSettings }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Reading Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor,
                    actionIconContentColor = textColor
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding)
                .padding(padding)
        ) {
            when {
                content == null -> {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading book...",
                                color = textColor,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                content?.contains("not yet supported") == true -> {
                    // Unsupported format
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(32.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = surfaceColor
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                                    text = "Format Not Supported",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = content ?: "",
                                    fontSize = 14.sp,
                                    color = textColor.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                else -> {
                    // Book content display
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 24.dp,
                            vertical = 16.dp
                        )
                    ) {
                        item {
                            Text(
                                text = content ?: "",
                                fontSize = fontSize,
                                lineHeight = fontSize * lineSpacing,
                                color = textColor,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Reading settings panel
            if (showSettings) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = surfaceColor
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "Reading Settings",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            // Font size setting
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatSize,
                                    contentDescription = "Font Size",
                                    tint = textColor
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Font Size",
                                    color = textColor,
                                    modifier = Modifier.weight(1f)
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    FilledTonalButton(
                                        onClick = {
                                            if (fontSize > 12.sp) fontSize = (fontSize.value - 2).sp
                                        },
                                        modifier = Modifier.size(36.dp),
                                        contentPadding = PaddingValues(0.dp),
                                        shape = CircleShape
                                    ) {
                                        Text("A", fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${fontSize.value.toInt()}",
                                        color = textColor,
                                        modifier = Modifier.widthIn(min = 24.dp),
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    FilledTonalButton(
                                        onClick = {
                                            if (fontSize < 24.sp) fontSize = (fontSize.value + 2).sp
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

                            // Dark mode toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Brightness6,
                                    contentDescription = "Theme",
                                    tint = textColor
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Dark Mode",
                                    color = textColor,
                                    modifier = Modifier.weight(1f)
                                )
                                Switch(
                                    checked = isDarkMode,
                                    onCheckedChange = { isDarkMode = it }
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Line spacing setting
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Line Spacing",
                                        color = textColor,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "${(lineSpacing * 100).toInt()}%",
                                        color = textColor
                                    )
                                }
                                Slider(
                                    value = lineSpacing,
                                    onValueChange = { lineSpacing = it },
                                    valueRange = 1.0f..2.0f,
                                    steps = 4,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Close button
                            FilledTonalButton(
                                onClick = { showSettings = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Done")
                            }
                        }
                    }
                }
            }
        }
    }
}