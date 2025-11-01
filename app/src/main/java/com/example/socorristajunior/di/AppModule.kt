package com.example.socorristajunior.di

import android.content.Context
import androidx.room.Room
import com.example.socorristajunior.Data.BancoDeDados.AppDatabase
import com.example.socorristajunior.Data.BancoDeDados.PrepopulateDatabaseCallback
import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Data.DAO.QuizDAO
import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Domain.Repositorio.CadastroRepositorio
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo
import com.example.socorristajunior.Domain.Repositorio.QuizRepo
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
    @Singleton // Use @Singleton para manter o padrão
    fun provideUserDao(appDatabase: AppDatabase): UserDAO {
        return appDatabase.userDAO()
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
/*
    @Provides
    @Singleton
    fun provideCadastroRepo(): CadastroRepositorio {
        // Como o repositório não tem dependências,
        // podemos simplesmente criar uma nova instância
        return CadastroRepositorio()
    }*/

    @Provides
    @Singleton
    fun provideQuizDao(appDatabase: AppDatabase): QuizDAO {
        // Pega o DAO da instância principal do banco de dados
        return appDatabase.quizDAO()
    }

    @Provides
    @Singleton
    fun provideQuizRepo(quizDAO: QuizDAO): QuizRepo {
        // Cria o repositório injetando o DAO (que o Hilt aprendeu a criar acima)
        return QuizRepo(quizDAO)
    }
}