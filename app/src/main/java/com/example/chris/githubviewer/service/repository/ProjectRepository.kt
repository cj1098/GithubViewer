package com.example.chris.githubviewer.service.repository

import com.example.chris.githubviewer.INTIAL_ITEMS_COUNT
import com.example.chris.githubviewer.service.ApiServiceInterface
import com.example.chris.githubviewer.model.GithubResult
import io.reactivex.Observable
import javax.inject.Inject

class ProjectRepository @Inject constructor(private val apiServiceInterface: ApiServiceInterface) {
    fun getRepositories(keyword: String): Observable<GithubResult> {
        return apiServiceInterface.searchGithubRespositories(keyword, INTIAL_ITEMS_COUNT)
    }
}