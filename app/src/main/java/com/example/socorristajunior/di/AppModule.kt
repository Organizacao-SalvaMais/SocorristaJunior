package com.example.socorristajunior.di

import android.content.Context
import androidx.room.Room
import com.example.socorristajunior.Data.BancoDeDados.AppDatabase
import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "socorrista_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEmergenciaDao(appDatabase: AppDatabase): EmergenciaDAO {
        return appDatabase.emergenciaDAO()
    }

    @Provides
    @Singleton
    fun providePassoDao(appDatabase: AppDatabase): PassoDAO {
        return appDatabase.passoDAO()
    }

    @Provides
    @Singleton
    fun provideEmergenciaRepo(emergenciaDAO: EmergenciaDAO): EmergenciaRepo {
        return EmergenciaRepo(emergenciaDAO)
    }

    @Provides

    fun providePassoRepo(passoDAO: PassoDAO): PassoRepo {
        return PassoRepo(passoDAO)
    }
}