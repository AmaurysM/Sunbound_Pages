package com.amaurysdelossantos.project.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.amaurysdelossantos.project.model.WebtoonEpisode
import com.amaurysdelossantos.project.model.WebtoonPanel
import com.amaurysdelossantos.project.model.complexRelations.WebtoonEpisodeWithPanels
import kotlinx.coroutines.flow.Flow

@Dao
interface WebtoonDao {

    @Query("SELECT * FROM webtoon_episodes WHERE bookId = :bookId AND isDeleted = 0 ORDER BY episodeNumber ASC")
    fun getEpisodesByBookId(bookId: String): Flow<List<WebtoonEpisode>>

    @Query("SELECT * FROM webtoon_episodes WHERE id = :episodeId AND isDeleted = 0")
    suspend fun getEpisodeById(episodeId: String): WebtoonEpisode?

    @Transaction
    @Query("SELECT * FROM webtoon_episodes WHERE id = :episodeId AND isDeleted = 0")
    suspend fun getEpisodeWithPanels(episodeId: String): WebtoonEpisodeWithPanels?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: WebtoonEpisode)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<WebtoonEpisode>)

    @Query("SELECT * FROM webtoon_panels WHERE episodeId = :episodeId AND isDeleted = 0 ORDER BY `order` ASC")
    fun getPanelsByEpisodeId(episodeId: String): Flow<List<WebtoonPanel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPanel(panel: WebtoonPanel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPanels(panels: List<WebtoonPanel>)

    @Query("SELECT COUNT(*) FROM webtoon_episodes WHERE bookId = :bookId AND isDeleted = 0")
    suspend fun getEpisodeCountByBookId(bookId: String): Int

    // Get free vs paid episodes
    @Query("SELECT * FROM webtoon_episodes WHERE bookId = :bookId AND isFree = :isFree AND isDeleted = 0 ORDER BY episodeNumber ASC")
    fun getEpisodesByFreeStatus(bookId: String, isFree: Boolean): Flow<List<WebtoonEpisode>>
}