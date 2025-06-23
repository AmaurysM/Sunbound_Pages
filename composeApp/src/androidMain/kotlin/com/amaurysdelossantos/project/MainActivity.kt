package com.amaurysdelossantos.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.amaurysdelossantos.project.di.initializeKoin
import com.amaurysdelossantos.project.navigation.RootComponent
import com.arkivanov.decompose.retainedComponent
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initializeKoin {
            androidContext(this@MainActivity)
        }

        val root = retainedComponent {
            RootComponent(it)
        }
        setContent {
            App(root)
        }
    }
}