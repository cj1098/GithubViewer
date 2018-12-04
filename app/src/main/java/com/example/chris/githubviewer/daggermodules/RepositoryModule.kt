package com.example.chris.githubviewer.daggermodules

import com.example.chris.githubviewer.service.ApiServiceInterface
import com.example.chris.githubviewer.service.repository.ProjectRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class RepositoryModule {
    @Provides
    @Singleton
    fun providesProjectRepository(apiServiceInterface: ApiServiceInterface): ProjectRepository {
        return ProjectRepository(apiServiceInterface)
    }
}