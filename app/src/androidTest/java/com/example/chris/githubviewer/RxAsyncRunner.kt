package com.example.chris.githubviewer

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import androidx.test.runner.AndroidJUnitRunner
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

class RxAsyncRunner : AndroidJUnitRunner() {

    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, GithubViewerApplication::class.java.name, context)
    }

    override fun onStart() {
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR) }
        RxJavaPlugins.setInitComputationSchedulerHandler { Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR) }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR) }
        RxJavaPlugins.setInitSingleSchedulerHandler { Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR) }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR) }
        super.onStart()
    }
}