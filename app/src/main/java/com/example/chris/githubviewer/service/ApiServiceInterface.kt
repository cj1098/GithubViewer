package com.example.chris.githubviewer.service

import com.example.chris.githubviewer.model.GithubResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceInterface {
    @GET("search/repositories")
    fun searchGithubRespositories(@Query("q") q: String, @Query("per_page") amount: Int): Observable<GithubResult>

}
