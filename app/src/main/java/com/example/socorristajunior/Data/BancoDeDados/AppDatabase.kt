package com.example.socorristajunior.Data.BancoDeDados

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Data.model.Passo

// Para adicionar uma nova tabela: adicione a nova classe na lista entities
@Database(entities = [Emergencia::class, Passo::class], version = 2) // Acrestente 1 no version
abstract class AppDatabase : RoomDatabase(){
    // Adicione seguindo o seguinte modelo
    abstract fun EmergenciaDAO() : EmergenciaDAO
    abstract fun PassoDAO() : PassoDAO
}