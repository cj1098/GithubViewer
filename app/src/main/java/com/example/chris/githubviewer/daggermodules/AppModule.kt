package com.example.chris.githubviewer.daggermodules

import com.example.chris.githubviewer.service.ApiServiceInterface
import com.example.chris.githubviewer.BASE_URL
import com.example.chris.githubviewer.GithubViewerApplication
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(val application: GithubViewerApplication) {
    @Provides
    @Singleton
    fun provideApplication() = application

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
    }

    @Provides
    @Singleton
    fun provideApiServiceInterface(retrofit: Retrofit): ApiServiceInterface {
        return retrofit.create(ApiServiceInterface::class.java)
    }
}