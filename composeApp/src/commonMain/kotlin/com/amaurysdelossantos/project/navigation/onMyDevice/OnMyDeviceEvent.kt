package com.amaurysdelossantos.project.navigation.onMyDevice

interface OnMyDeviceEvent {
    data class SearchQueryChanged(val query: String) : OnMyDeviceEvent
    object BackClicked : OnMyDeviceEvent
    object CancelSearch : OnMyDeviceEvent
    object OpenFileExplorer : OnMyDeviceEvent
}