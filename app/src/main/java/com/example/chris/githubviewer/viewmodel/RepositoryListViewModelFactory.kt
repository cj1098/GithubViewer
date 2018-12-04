package com.example.chris.githubviewer.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class RepositoryListViewModelFactory @Inject constructor(
        private val repositoryListViewModel: RepositoryListViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepositoryListViewModel::class.java!!)) {
            return repositoryListViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}