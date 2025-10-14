package com.example.socorristajunior.di

import android.content.Context
import androidx.room.Room
import com.example.socorristajunior.Data.BancoDeDados.AppDatabase
import com.example.socorristajunior.Data.BancoDeDados.PrepopulateDatabaseCallback
import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePrepopulateCallback(
        @ApplicationContext context: Context,
        dbProvider: Provider<AppDatabase>
    ): PrepopulateDatabaseCallback {
        return PrepopulateDatabaseCallback(context, dbProvider)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        callback: PrepopulateDatabaseCallback
        ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "socorrista_db"
        )
            .addCallback(callback)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideEmergenciaDao(appDatabase: AppDatabase): EmergenciaDAO {
        // Retorna a implementação do DAO de Emergência a partir da instância do banco.
        return appDatabase.emergenciaDAO()
    }

    @Provides
    @Singleton
    fun providePassoDao(appDatabase: AppDatabase): PassoDAO {
        // Retorna a implementação do DAO de Passo a partir da instância do banco.
        return appDatabase.passoDAO()
    }

    @Provides
    @Singleton
    fun provideEmergenciaRepo(emergenciaDAO: EmergenciaDAO): EmergenciaRepo {
        // Retorna uma instância do repositório, injetando o DAO.
        return EmergenciaRepo(emergenciaDAO)
    }

    @Provides
    @Singleton
    fun providePassoRepo(passoDAO: PassoDAO): PassoRepo {
    // Retorna uma instância do repositório, injetando o DAO.
        return PassoRepo(passoDAO)
    }
}