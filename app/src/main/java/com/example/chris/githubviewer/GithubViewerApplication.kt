package com.example.chris.githubviewer

import android.app.Activity
import android.app.Application
import com.example.chris.githubviewer.daggermodules.AppModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class GithubViewerApplication: Application(), HasActivityInjector {
    @Inject
    lateinit var activityDispatchingAndroidInject: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInject
    }


    companion object {
        @JvmStatic lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerApplicationComponent.builder()
                .appModule(AppModule(this))
                .build()
        graph.inject(this)
    }

}