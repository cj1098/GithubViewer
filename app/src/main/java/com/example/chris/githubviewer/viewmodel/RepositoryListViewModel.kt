package com.example.chris.githubviewer.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.chris.githubviewer.model.GithubResult
import com.example.chris.githubviewer.service.repository.ProjectRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RepositoryListViewModel @Inject constructor(private val repository: ProjectRepository): ViewModel() {
    val githubResult: MutableLiveData<GithubResult> = MutableLiveData()
    val githubError: MutableLiveData<String> = MutableLiveData()
    lateinit var disposable: Disposable

    fun githubResult(): LiveData<GithubResult> = githubResult
    fun githubError(): LiveData<String> = githubError

    fun loadGithubResults(keyword: String) {
        disposable = repository.getRepositories(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    githubResult.postValue(it)
                }, {
                    githubError.postValue(it.message)
                })
    }

    override fun onCleared() {
        super.onCleared()
        if (::disposable.isInitialized) {
            disposable.dispose()
        }
    }

    fun dispose() {
        if (::disposable.isInitialized && !disposable.isDisposed) disposable.dispose()
    }
}