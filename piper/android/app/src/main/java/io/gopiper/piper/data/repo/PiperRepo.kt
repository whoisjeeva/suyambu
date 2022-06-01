package io.gopiper.piper.data.repo

import io.gopiper.piper.data.source.PiperDao
import io.gopiper.piper.model.PiperLog
import io.gopiper.piper.model.Pipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PiperRepo @Inject constructor(private val piperDao: PiperDao) {
    fun allLogs(scriptId: String): Flow<List<PiperLog>> = piperDao.allLogs(scriptId).flowOn(Dispatchers.IO)
        .conflate()

    suspend fun insertLog(piperLog: PiperLog) = piperDao.insertLog(piperLog)

    suspend fun deleteLogs(scriptId: String) = piperDao.deleteLogs(scriptId)

    fun allPipes(): Flow<List<Pipe>> = piperDao.allPipes().flowOn(Dispatchers.IO)
        .conflate()

    fun filterPipes(query: String): Flow<List<Pipe>> = piperDao.filterPipes(query)
        .flowOn(Dispatchers.IO).conflate()


    suspend fun getPipe(scriptId: String) = piperDao.getPipe(scriptId)

    suspend fun deletePipe(pipe: Pipe) = piperDao.deletePipe(pipe)

    suspend fun insertPipe(pipe: Pipe) = piperDao.insertPipe(pipe)

    suspend fun updatePipe(pipe: Pipe) = piperDao.updatePipe(pipe)

    suspend fun deleteAllPipes() = piperDao.deleteAllPipes()
}