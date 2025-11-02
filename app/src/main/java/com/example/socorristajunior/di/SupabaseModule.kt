package com.example.socorristajunior.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient

import javax.inject.Singleton

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest



@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient =
        createSupabaseClient(
            supabaseUrl = "https://shgsemlmtyelclauujjc.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNoZ3NlbWxtdHllbGNsYXV1ampjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjEwMzg1ODYsImV4cCI6MjA3NjYxNDU4Nn0.bfbxhNYn6K5owK0MnAg8vf3pXekb31w-bY6S7mOQJ8E"
        ) {
            install(Postgrest)
        }
}