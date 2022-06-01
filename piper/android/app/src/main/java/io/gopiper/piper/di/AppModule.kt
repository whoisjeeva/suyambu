package io.gopiper.piper.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.gopiper.piper.data.repo.PiperRepo
import io.gopiper.piper.data.source.PiperDao
import io.gopiper.piper.data.source.PiperDatabase
import io.gopiper.piper.engine.EditorHolder
import me.gumify.hiper.util.Tart
import me.gumify.hiper.util.WeeDB
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideEditorHolder(@ApplicationContext context: Context) = EditorHolder(context)

    @Singleton
    @Provides
    fun provideWeeDB(@ApplicationContext context: Context) = WeeDB(context)

    @Singleton
    @Provides
    fun providePiperDatabase(@ApplicationContext context: Context) = PiperDatabase.getInstance(context)

    @Singleton
    @Provides
    fun providePiperDao(piperDatabase: PiperDatabase) = piperDatabase.piperDao()

    @Singleton
    @Provides
    fun providePiperRepo(piperDao: PiperDao) = PiperRepo(piperDao)

    @Singleton
    @Provides
    fun provideTart(@ApplicationContext context: Context) = Tart(context, "piper")
}