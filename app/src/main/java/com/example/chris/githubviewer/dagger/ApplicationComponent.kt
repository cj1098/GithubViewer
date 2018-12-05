package com.example.chris.githubviewer.dagger

import com.example.chris.githubviewer.GithubViewerApplication
import com.example.chris.githubviewer.list.RepositoryListFragment
import com.example.chris.githubviewer.MainActivity
import com.example.chris.githubviewer.dagger.daggermodules.ActivityModule
import com.example.chris.githubviewer.dagger.daggermodules.AppModule
import com.example.chris.githubviewer.dagger.daggermodules.ContextModule
import com.example.chris.githubviewer.dagger.daggermodules.ViewModelModule
import com.example.chris.githubviewer.viewmodel.RepositoryListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ContextModule::class, ViewModelModule::class, ActivityModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(repositoryListViewModel: RepositoryListViewModel)
    fun inject(repositoryListFragment: RepositoryListFragment)
    fun inject(githubViewerApplication: GithubViewerApplication)
}