package com.example.socorristajunior.Data.BancoDeDados

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModulo {
    @Provides
    @Singleton
    fun Appconexao(
        @ApplicationContext
        context: Context)= Room.databaseBuilder(
            context = context,
        AppDatabase::class.java,
            "socorristaJunior.db"
        ).build()

    @Provides
    @Singleton
    fun getEmergenciaDAO(db: AppDatabase) = db.EmergenciaDAO()

    @Provides
    @Singleton
    fun getPassoDAO(db: AppDatabase) = db.PassoDAO()
}