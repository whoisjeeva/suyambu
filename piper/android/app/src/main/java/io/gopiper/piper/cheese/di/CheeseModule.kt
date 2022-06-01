package io.gopiper.piper.cheese.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.gopiper.piper.cheese.Browser
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CheeseModule {
    @Singleton
    @Provides
    fun provideBrowser(@ApplicationContext context: Context) = Browser(context = context)
}