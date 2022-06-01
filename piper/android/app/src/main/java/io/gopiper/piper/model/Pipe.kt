package io.gopiper.piper.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pipe")
data class Pipe(
    val scriptId: String,
    var title: String,
    var fileName: String,
    var workspace: String,
    var code: String,
    var variableStack: String,
    var isRunning: Boolean = false,
    var UA: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}