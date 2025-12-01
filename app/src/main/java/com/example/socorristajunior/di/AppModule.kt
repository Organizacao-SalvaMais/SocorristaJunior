package com.example.socorristajunior.di

import android.content.Context
import androidx.room.Room
import com.example.socorristajunior.Data.BancoDeDados.AppDatabase
import com.example.socorristajunior.Data.BancoDeDados.PrepopulateDatabaseCallback
import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DAO.OptionDAO
import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Data.DAO.QuestionDAO
import com.example.socorristajunior.Data.DAO.QuizCategoryDAO
import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo
import com.example.socorristajunior.Domain.Repositorio.QuizRepo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
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
        return PrepopulateDatabaseCallback(
            context,
            dbProvider
        )
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        callback: Provider<PrepopulateDatabaseCallback>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "socorrista_db"
        )
            .addCallback(callback.get())
            .fallbackToDestructiveMigration()
            .build()
    }

    // --- PROVEDORES DE DAO ---

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
    fun provideQuizCategoryDao(appDatabase: AppDatabase): QuizCategoryDAO {
        return appDatabase.quizCategoryDAO()
    }

    @Provides
    @Singleton
    fun provideQuestionDao(appDatabase: AppDatabase): QuestionDAO {
        return appDatabase.questionDAO()
    }

    @Provides
    @Singleton
    fun provideOptionDao(appDatabase: AppDatabase): OptionDAO {
        return appDatabase.optionDAO()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDAO {
        return appDatabase.userDAO()
    }

    // --- PROVEDORES DE REPOSITÓRIO ---

    @Provides
    @Singleton
    fun provideEmergenciaRepo(
        supabaseClient: SupabaseClient,
        emergenciaDAO: EmergenciaDAO,
        passoDAO: PassoDAO
    ): EmergenciaRepo {
        return EmergenciaRepo(
            supabaseClient,
            emergenciaDAO,
            passoDAO
        )
    }

    @Provides
    @Singleton
    fun providePassoRepo(passoDAO: PassoDAO): PassoRepo {
        return PassoRepo(passoDAO)
    }

    @Provides
    @Singleton
    fun provideQuizRepo(quizCategoryDAO: QuizCategoryDAO): QuizRepo {
        return QuizRepo(quizCategoryDAO)
    }

    // --- PROVEDOR DE AUTENTICAÇÃO ---
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

   /* @Provides
    @Singleton
    fun provideEmergencyContactsViewModel(): EmergencyContactsViewModel {
        return EmergencyContactsViewModel()
    }
    */
}