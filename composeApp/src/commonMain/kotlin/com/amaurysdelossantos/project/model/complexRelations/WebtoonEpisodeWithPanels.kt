package com.amaurysdelossantos.project.model.complexRelations

import androidx.room.Embedded
import androidx.room.Relation
import com.amaurysdelossantos.project.model.WebtoonEpisode
import com.amaurysdelossantos.project.model.WebtoonPanel

data class WebtoonEpisodeWithPanels(
    @Embedded val episode: WebtoonEpisode,
    @Relation(
        parentColumn = "id",
        entityColumn = "episodeId"
    )
    val panels: List<WebtoonPanel>
)
