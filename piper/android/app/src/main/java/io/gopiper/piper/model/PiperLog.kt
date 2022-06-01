package io.gopiper.piper.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "piper_log")
data class PiperLog(
    val scriptId: String,
    val value: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}