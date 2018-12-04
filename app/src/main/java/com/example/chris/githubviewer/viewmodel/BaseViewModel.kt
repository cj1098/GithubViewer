package com.example.chris.githubviewer.viewmodel

import android.arch.lifecycle.ViewModel
import com.example.chris.githubviewer.service.ApiServiceInterface
import javax.inject.Inject

abstract class BaseViewModel: ViewModel() {
    @Inject
    lateinit var apiServiceInterface: ApiServiceInterface
}