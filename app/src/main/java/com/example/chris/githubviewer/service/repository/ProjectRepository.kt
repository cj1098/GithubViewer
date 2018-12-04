package com.example.chris.githubviewer.service.repository

import com.example.chris.githubviewer.ApiServiceInterface
import com.example.chris.githubviewer.model.GithubResult
import io.reactivex.Observable
import javax.inject.Inject

class ProjectRepository @Inject constructor(val apiServiceInterface: ApiServiceInterface) {
    fun getRepositories(keyword: String): Observable<GithubResult> {
        return apiServiceInterface.searchGithubRespositories(keyword, 20)
    }
}