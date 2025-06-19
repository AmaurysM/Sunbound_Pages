package com.amaurysdelossantos.project.navigation.bookInfo

import com.amaurysdelossantos.project.navigation.finishedbooks.FinishedBooksEvent

interface BookInfoEvent {
    object BackClicked : BookInfoEvent
}