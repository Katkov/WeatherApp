package com.example.weatherapp.di

import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.networking.NetworkingService
import com.example.weatherapp.provider.CoroutineDispatcherProvider
import com.example.weatherapp.provider.DispatcherProvider
import com.example.weatherapp.repository.RemoteRepository
import com.example.weatherapp.repository.Repository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideRetrofit(): NetworkingService =
        Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .build()
            .create(NetworkingService::class.java)

    @Provides
    fun provideRepository(networkingService: NetworkingService): Repository =
        RemoteRepository(networkingService)

    @Provides
    fun provideDispatcher(): DispatcherProvider = CoroutineDispatcherProvider()

    private fun getOkHttpClient() =
        OkHttpClient.Builder()
            .connectTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    private fun getGson() =
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm")
            .create();

    companion object {
        const val NETWORK_REQUEST_TIMEOUT_SECONDS = 15L
        const val BASE_URL = "http://api.weatherapi.com/v1/"
    }
}