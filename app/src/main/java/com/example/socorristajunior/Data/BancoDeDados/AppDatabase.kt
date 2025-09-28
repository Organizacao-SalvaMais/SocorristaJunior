package com.example.socorristajunior.Data.BancoDeDados

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Data.model.Passo

@Database(
    entities = [Emergencia::class, Passo::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emergenciaDAO(): EmergenciaDAO
    abstract fun passoDAO(): PassoDAO
}