package com.amaurysdelossantos.project.model

import com.amaurysdelossantos.project.database.enums.MediaType

data class MediaTypeCount(
    val mediaType: MediaType,
    val count: Int
)