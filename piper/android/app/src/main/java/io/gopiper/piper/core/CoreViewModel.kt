package io.gopiper.piper.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.gopiper.piper.data.repo.PiperRepo
import io.gopiper.piper.engine.EditorHolder
import io.gopiper.piper.model.Pipe
import io.gopiper.piper.model.PiperLog
import io.gopiper.piper.util.C
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.gumify.hiper.util.Tart
import me.gumify.hiper.util.WeeDB
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@HiltViewModel
class CoreViewModel @Inject constructor(
    private val piperRepo: PiperRepo,
    private val wee: WeeDB,
    private val tart: Tart
): ViewModel() {
    val theme = tart.getInt("theme")

    fun getUA() = wee.getString("UA")

    fun allPipes() = piperRepo.allPipes()

    fun allLogs(scriptId: String) = piperRepo.allLogs(scriptId)

    fun insertPipe(pipe: Pipe) {
        viewModelScope.launch {
            piperRepo.insertPipe(pipe)
        }
    }

    fun filterPipes(query: String) = piperRepo.filterPipes(query)

    fun createScript(title: String, fileName: String, onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val scriptId = UUID.randomUUID().toString()
            piperRepo.insertPipe(Pipe(
                scriptId = scriptId,
                title = title,
                fileName = fileName,
                workspace = "",
                code = "[]",
                variableStack = "{}"
            ))
            onCreated(scriptId)
        }
    }

    fun updateTitleAndFileName(scriptId: String, title: String, fileName: String) {
        viewModelScope.launch {
            piperRepo.getPipe(scriptId)?.also {
                it.title = title
                it.fileName = fileName
                piperRepo.updatePipe(it)
            }
        }
    }

    fun deletePipe(scriptId: String) {
        viewModelScope.launch {
            piperRepo.getPipe(scriptId)?.also {
                piperRepo.deletePipe(it)
            }
        }
    }

    fun updateLog(scriptId: String, log: String) {
        viewModelScope.launch {
            piperRepo.insertLog(PiperLog(scriptId, log))
        }
    }

    fun deleteLogs(scriptId: String) {
        viewModelScope.launch {
            piperRepo.deleteLogs(scriptId)
        }
    }

    fun saveScript(
        scriptId: String,
        code: String,
        workspace: String,
        variableStack: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            val pipe = piperRepo.getPipe(scriptId)
            pipe?.code = code
            pipe?.workspace = workspace
            pipe?.variableStack = variableStack
            if (pipe != null) {
                piperRepo.updatePipe(pipe)
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    suspend fun getPipe(scriptId: String) = piperRepo.getPipe(scriptId)

    fun storeScriptId(scriptId: String) {
        wee.put("scriptId", scriptId)
    }

    fun storeDebugCode(code: String) {
        wee.put("debug_code", code)
    }

    fun getDebugCode() = wee.getString("debug_code") ?: "[]"

    fun getStoredScriptId(): String {
        return wee.getString("scriptId").toString()
    }

    fun isToolbarToggleVisible(): Boolean {
        return wee.getBoolean(C.SETTING_SHOW_EDITOR_HANDLE)
    }
}