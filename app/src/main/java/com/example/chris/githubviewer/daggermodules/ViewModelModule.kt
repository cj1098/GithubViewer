package com.example.chris.githubviewer.daggermodules

import android.arch.lifecycle.ViewModelProvider
import com.example.chris.githubviewer.viewmodel.RepositoryListViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {
    @Provides
    @Singleton
    fun provideRepositoryListViewModelFactory(factory: RepositoryListViewModelFactory): ViewModelProvider.Factory = factory
}