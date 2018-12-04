package com.example.chris.githubviewer.dagger.daggermodules

import com.example.chris.githubviewer.GithubViewerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module (includes = [AppModule::class])
class ContextModule {
    @Provides
    @Singleton
    fun providesContext(application: GithubViewerApplication) = application

}