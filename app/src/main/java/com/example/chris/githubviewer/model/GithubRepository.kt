package com.example.chris.githubviewer.model

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
class Owner(val login: String, val avatar_url: String): Parcelable {
    constructor() : this("", "")
}

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubRepository(
        val name: String,
        val owner: Owner,
        val description: String,
        val size: Int,
        val forks_count: Int,
        val open_issues_count: Int,
        val url: String): Parcelable {
    constructor() : this("", Owner(), "", 0, 0, 0, "")
}

@JsonIgnoreProperties(ignoreUnknown = true)
class GithubResult(val total_count: Int, val incomplete_results: Boolean, var items: List<GithubRepository>) {
    constructor() : this(0, false, arrayListOf())
}