package com.example.socorristajunior.Data.BancoDeDados

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Data.model.UserEntity

@Database(
    entities = [
        Emergencia::class,
        Passo::class,
        UserEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun emergenciaDAO(): EmergenciaDAO
    abstract fun passoDAO(): PassoDAO
}