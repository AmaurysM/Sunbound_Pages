package com.amaurysdelossantos.project.navigation.library

import com.arkivanov.decompose.ComponentContext

class LibraryComponent(
    componentContext: ComponentContext,
    private val onMyDeviceButton: () -> Unit
) : ComponentContext by componentContext {

    fun onEvent(event: LibraryEvent) {
        when (event) {
            LibraryEvent.ImportFromFiles -> TODO()
            LibraryEvent.OnMyDevice -> {
                onMyDeviceButton()
            }

            LibraryEvent.WebServer -> TODO()
        }
    }
}