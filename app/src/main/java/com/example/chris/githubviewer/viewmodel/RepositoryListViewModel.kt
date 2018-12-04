package com.example.chris.githubviewer.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.chris.githubviewer.model.GithubRepository
import com.example.chris.githubviewer.model.GithubResult
import com.example.chris.githubviewer.service.repository.ProjectRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RepositoryListViewModel @Inject constructor(private val repository: ProjectRepository): BaseViewModel() {
    val githubResult: MutableLiveData<GithubResult> = MutableLiveData()
    val githubError: MutableLiveData<String> = MutableLiveData()
    lateinit var disposable: Disposable

    fun githubResult(): LiveData<GithubResult> = githubResult
    fun githubError(): LiveData<String> = githubError

    fun loadGithubResults(keyword: String) {

        disposable = repository.apiServiceInterface.searchGithubRespositories(keyword, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    githubResult.value = it
                }, {
                    githubError.value = it.message
                })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun dispose() {
        if (!disposable.isDisposed) disposable.dispose()
    }
}