package com.example.studytimetwo.di

import android.content.Context
import com.example.studytimetwo.data.StudyDao
import com.example.studytimetwo.data.StudyDatabase
import com.example.studytimetwo.data.repository.StudyRepoImpl
import com.example.studytimetwo.data.repository.StudyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module

object ApplicationModule {
    @Singleton
    @Provides
    fun provideStudyDao(db: StudyDatabase) = db.studyDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = StudyDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideRepository(dao: StudyDao): StudyRepository = StudyRepoImpl(dao)
}