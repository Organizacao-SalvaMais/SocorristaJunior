package com.example.socorristajunior.di

import com.example.socorristajunior.Data.remote.NoticiasApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //Aqui está utilizando o IP da minha máquina
    //Vai ser substituído por "https://api.salvarmais.cloud/" ao fazer o deploy
    private const val BASE_URL = "http://192.168.15.7:8000/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNoticiasApi(retrofit: Retrofit): NoticiasApi {
        return retrofit.create(NoticiasApi::class.java)
    }
}