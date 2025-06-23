package com.amaurysdelossantos.project

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

actual val world: String
    get() = "Working with android"

@Composable
actual fun PlatformFunction() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        while (true) {
            Toast.makeText(context, "This is a marquee", Toast.LENGTH_SHORT).show()
            delay(2000)
            Toast.makeText(context, "For democracy", Toast.LENGTH_SHORT).show()
            delay(2000)

        }
    }
}