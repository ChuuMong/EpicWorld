package com.chuumong.games.di

import com.chuumong.games.BuildConfig
import com.chuumong.games.data.remote.GameApiService
import com.chuumong.games.data.remote.interceptor.GameApiInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesOkHttpClient() = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
        )
        .addNetworkInterceptor(GameApiInterceptor())
        .build()

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .client(okHttpClient)
        .build()

    @Provides
    fun provideGameApiService(retrofit: Retrofit) = retrofit.create(GameApiService::class.java)
}