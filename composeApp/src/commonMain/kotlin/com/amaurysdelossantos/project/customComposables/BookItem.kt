package com.amaurysdelossantos.project.customComposables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.amaurysdelossantos.project.model.Book
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay

@Composable
fun BookItem(
    book: Book,
    size: Dp = 150.dp,
    isDeleteMode: Boolean = false,
    onDeleteModeChange: (Boolean) -> Unit = {},
    onDelete: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    // Memoized constants to avoid recomposition
    val dimensions = remember(size) {
        BookDimensions(
            width = size,
            height = size * 1.5f
        )
    }

    val hapticFeedback = LocalHapticFeedback.current

    // State management
    val bookState = remember {
        BookItemState()
    }

    // Animation definitions
    val animations = rememberBookAnimations(
        isDeleteMode = bookState.localDeleteMode,
        isPressed = bookState.isPressed
    )

    // Long press handler
    LaunchedEffect(bookState.isHolding) {
        if (bookState.isHolding) {
            delay(600) // Reduced from 1000ms for better UX
            if (bookState.isHolding && !bookState.localDeleteMode) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                bookState.enterDeleteMode()
                onDeleteModeChange(true)
            }
        }
    }

    // Reset local delete mode when global delete mode is disabled
    LaunchedEffect(isDeleteMode) {
        if (!isDeleteMode) {
            bookState.exitDeleteMode()
        }
    }

    Box(
        modifier = Modifier
            .width(dimensions.width)
            .height(dimensions.height)
    ) {
        BookCard(
            book = book,
            dimensions = dimensions,
            bookState = bookState,
            animations = animations,
            onClick = onClick,
            onDeleteModeChange = {
                if (bookState.localDeleteMode) {
                    bookState.exitDeleteMode()
                } else {
                    bookState.enterDeleteMode()
                }
                onDeleteModeChange(bookState.localDeleteMode)
            }
        )


        if (bookState.localDeleteMode) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 8.dp, y = (-8).dp)
                    .size(28.dp)
                    .scale(animations.deleteButtonScale)
                    .rotate(animations.deleteButtonRotation)
                    .zIndex(10f)
                    .clickable {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        bookState.exitDeleteMode()
                        onDeleteModeChange(false)
                        onDelete()
                    },
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete ${book.title}",
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookCard(
    book: Book,
    dimensions: BookDimensions,
    bookState: BookItemState,
    animations: BookAnimations,
    onClick: () -> Unit,
    onDeleteModeChange: (Boolean) -> Unit // add this callback to control delete mode
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .scale(animations.pressScale)
            .graphicsLayer {
                if (bookState.localDeleteMode) {
                    rotationZ = animations.jiggleRotation
                    translationX = animations.jiggleTranslationX
                }
            }
            .shadow(
                elevation = if (bookState.localDeleteMode) 16.dp else 8.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        // Enter delete mode
                        if (!bookState.localDeleteMode) {
                            onDeleteModeChange(true)
                        }
                    },
                    onTap = {
                        if (bookState.localDeleteMode) {
                            // Exit delete mode
                            onDeleteModeChange(false)
                        } else {
                            onClick()
                        }
                    },
                    onPress = {
                        bookState.onPressStart()
                        try {
                            awaitRelease()
                        } catch (_: Exception) {
                        }
                        bookState.onPressEnd()
                    }
                )
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (bookState.localDeleteMode) 16.dp else 6.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            BookCover(
                book = book,
                dimensions = dimensions
            )

            BookTitleOverlay(
                title = book.title,
                isVisible = book.coverImageUrl.isNullOrEmpty()
            )
        }
    }
}


@Composable
private fun BookCover(
    book: Book,
    dimensions: BookDimensions
) {
    if (!book.coverImageUrl.isNullOrEmpty()) {
        KamelImage(
            resource = asyncPainterResource(book.coverImageUrl!!),
            contentDescription = "Cover of ${book.title}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(18.dp)),
            onFailure = {
                BookPlaceholder(book.title)
            },
            onLoading = {
                LoadingPlaceholder()
            }
        )
    } else {
        BookPlaceholder(book.title)
    }
}

@Composable
private fun BookTitleOverlay(
    title: String,
    isVisible: Boolean
) {
    if (!isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 0.6f,
                        endY = 1f
                    ),
                    shape = RoundedCornerShape(18.dp)
                )
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

//@Composable
//private fun DeleteButton(
//    visible: Boolean,
//    scale: Float,
//    rotation: Float,
//    onDelete: () -> Unit
//) {
//    if (visible) {
//        Card(
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .offset(x = 8.dp, y = (-8).dp)
//                .size(28.dp)
//                .scale(scale)
//                .rotate(rotation)
//                .zIndex(10f)
//                .clickable { onDelete() },
//            shape = CircleShape,
//            colors = CardDefaults.cardColors(
//                containerColor = MaterialTheme.colorScheme.error
//            ),
//            elevation = CardDefaults.cardElevation(
//                defaultElevation = 12.dp
//            )
//        ) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Close,
//                    contentDescription = "Delete ${book.title}",
//                    tint = MaterialTheme.colorScheme.onError,
//                    modifier = Modifier.size(16.dp)
//                )
//            }
//        }
//    }
//}

@Composable
private fun BookPlaceholder(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title.take(15) + if (title.length > 15) "..." else "",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun LoadingPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                RoundedCornerShape(18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
    }
}

// Data classes and state management
@Stable
private data class BookDimensions(
    val width: Dp,
    val height: Dp
)

@Stable
private class BookItemState {
    var localDeleteMode by mutableStateOf(false)
        private set
    var isHolding by mutableStateOf(false)
        private set
    var isPressed by mutableStateOf(false)
        private set

    fun enterDeleteMode() {
        localDeleteMode = true
    }

    fun exitDeleteMode() {
        localDeleteMode = false
    }

    fun onPressStart() {
        isPressed = true
        isHolding = true
    }

    fun onPressEnd() {
        isPressed = false
        isHolding = false
    }
}

@Stable
private data class BookAnimations(
    val jiggleRotation: Float,
    val jiggleTranslationX: Float,
    val deleteButtonScale: Float,
    val deleteButtonRotation: Float,
    val pressScale: Float
)

@Composable
private fun rememberBookAnimations(
    isDeleteMode: Boolean,
    isPressed: Boolean
): BookAnimations {
    // Jiggle animation with improved timing
    val infiniteTransition = rememberInfiniteTransition(label = "jiggle")
    val jiggleRotation by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 140,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "jiggle_rotation"
    )

    val jiggleTranslationX by infiniteTransition.animateFloat(
        initialValue = -1.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 120,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "jiggle_translation"
    )

    // Delete button animations with improved springs
    val deleteButtonScale by animateFloatAsState(
        targetValue = if (isDeleteMode) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "delete_button_scale"
    )

    val deleteButtonRotation by animateFloatAsState(
        targetValue = if (isDeleteMode) 0f else 180f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "delete_button_rotation"
    )

    // Press scale with smoother animation
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "press_scale"
    )

    return BookAnimations(
        jiggleRotation = jiggleRotation,
        jiggleTranslationX = jiggleTranslationX,
        deleteButtonScale = deleteButtonScale,
        deleteButtonRotation = deleteButtonRotation,
        pressScale = pressScale
    )
}