package com.example.socorristajunior.Data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.socorristajunior.Data.model.Option

// DAO para as Opções
@Dao
interface OptionDAO {

    // Permite inserir/atualizar uma única opção
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(option: Option)

    // Insere uma lista de opções. Se já existir, substitui.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpcoes(opcoes: List<Option>)
}