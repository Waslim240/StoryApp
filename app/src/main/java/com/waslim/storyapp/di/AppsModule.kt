package com.waslim.storyapp.di

import android.content.Context
import com.waslim.storyapp.BuildConfig
import com.waslim.storyapp.model.datastore.DarkModeSettingPreferences
import com.waslim.storyapp.model.datastore.UserTokenPreferences
import com.waslim.storyapp.model.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppsModule {

    private const val URL = BuildConfig.BASE_URL

    private val loggingInterceptor = when {
        BuildConfig.DEBUG -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        else -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val client = OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) : ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesUserToken(@ApplicationContext context: Context) : UserTokenPreferences =
        UserTokenPreferences(context)

    @Singleton
    @Provides
    fun providesDarkMode(@ApplicationContext context: Context) : DarkModeSettingPreferences =
        DarkModeSettingPreferences(context)
}