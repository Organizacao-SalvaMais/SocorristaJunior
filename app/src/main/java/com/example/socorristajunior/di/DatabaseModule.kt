package com.example.socorristajunior.di
/*
import android.content.Context
import androidx.room.Room
import com.example.socorristajunior.Data.BancoDeDados.AppDatabase
import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DAO.PassoDAO
//import com.example.socorristajunior.Data.DAO.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "socorrista_junior_db"
        )
            // Adicionando .fallbackToDestructiveMigration() para evitar crashes
            // durante o desenvolvimento se você alterar o schema do banco.
            // Remova isso em produção se tiver um plano de migração manual.
            .fallbackToDestructiveMigration()
            .build()
    }
/*
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }
*/
    @Provides
fun provideEmergenciaDao(db: AppDatabase): EmergenciaDAO {
    return db.emergenciaDAO()
}

    @Provides
    fun providePassoDao(db: AppDatabase): PassoDAO {
        return db.passoDAO()
    }
}
*/