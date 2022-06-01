package io.gopiper.piper.data.source

import androidx.room.*
import io.gopiper.piper.model.PiperLog
import io.gopiper.piper.model.Pipe
import kotlinx.coroutines.flow.Flow

@Dao
interface PiperDao {
    @Query("SELECT * FROM piper_log WHERE scriptId = :scriptId ORDER BY id DESC")
    fun allLogs(scriptId: String): Flow<List<PiperLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(piperLog: PiperLog)

    @Query("DELETE FROM piper_log WHERE scriptId = :scriptId")
    suspend fun deleteLogs(scriptId: String)

    @Query("SELECT * FROM pipe ORDER BY id DESC")
    fun allPipes(): Flow<List<Pipe>>

    @Query("SELECT * FROM pipe WHERE title LIKE '%' || :query || '%' ORDER BY id DESC")
    fun filterPipes(query: String): Flow<List<Pipe>>

    @Query("SELECT * FROM pipe WHERE scriptId = :scriptId")
    suspend fun getPipe(scriptId: String): Pipe?

    @Delete
    suspend fun deletePipe(pipe: Pipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPipe(pipe: Pipe)

    @Update
    suspend fun updatePipe(pipe: Pipe)

    @Query("DELETE FROM pipe")
    suspend fun deleteAllPipes()
}