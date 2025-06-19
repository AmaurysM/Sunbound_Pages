package com.amaurysdelossantos.project.navigation.library

sealed interface LibraryEvent {
    object OnMyDevice : LibraryEvent
    object ImportFromFiles : LibraryEvent
    object WebServer : LibraryEvent
}