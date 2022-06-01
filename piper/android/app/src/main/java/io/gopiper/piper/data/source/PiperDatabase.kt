package io.gopiper.piper.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.gopiper.piper.data.util.Converters
import io.gopiper.piper.model.PiperLog
import io.gopiper.piper.model.Pipe

@Database(entities = [Pipe::class, PiperLog::class], version = 2)
@TypeConverters(Converters::class)
abstract class PiperDatabase: RoomDatabase() {
    abstract fun piperDao(): PiperDao

    companion object {
        private var INSTANCE: PiperDatabase? = null
        fun getInstance(context: Context): PiperDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context, PiperDatabase::class.java, "PiperDatabase")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}