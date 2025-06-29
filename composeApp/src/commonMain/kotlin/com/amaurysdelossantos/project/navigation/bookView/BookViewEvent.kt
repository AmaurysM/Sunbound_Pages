package com.amaurysdelossantos.project.navigation.bookView

interface BookViewEvent {
    object BackClicked : BookViewEvent
    object IsEBookClicked : BookViewEvent
    object IsComicClicked : BookViewEvent
    object IsAudiobookClicked : BookViewEvent

}