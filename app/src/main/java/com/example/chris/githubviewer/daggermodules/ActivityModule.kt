package com.example.chris.githubviewer.daggermodules

import com.example.chris.githubviewer.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}