package com.example.android_lab4.di

import com.example.android_lab4.data.database.NoteDatabase
import com.example.android_lab4.data.repository.NoteRepositoryImpl
import com.example.android_lab4.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(noteDatabase: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(noteDatabase.dao)
    }
}