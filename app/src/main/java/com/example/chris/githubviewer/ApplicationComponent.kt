package com.example.chris.githubviewer

import com.example.chris.githubviewer.daggermodules.ActivityModule
import com.example.chris.githubviewer.daggermodules.AppModule
import com.example.chris.githubviewer.daggermodules.ContextModule
import com.example.chris.githubviewer.daggermodules.ViewModelModule
import com.example.chris.githubviewer.viewmodel.BaseViewModel
import com.example.chris.githubviewer.viewmodel.RepositoryListViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ContextModule::class, ViewModelModule::class, ActivityModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(repositoryListViewModel: RepositoryListViewModel)
    fun inject(baseViewModel: BaseViewModel)
    fun inject(listFragment: ListFragment)
    fun inject(githubViewerApplication: GithubViewerApplication)
}