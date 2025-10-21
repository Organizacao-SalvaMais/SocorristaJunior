package com.example.socorristajunior.Data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergencia")
data class Emergencia(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "emercodigo")
    val emercodigo: Int = 0,

    @ColumnInfo(name = "emernome")
    val emernome: String,

    @ColumnInfo(name = "emerdesc")
    val emerdesc: String,

    @ColumnInfo(name = "emergravidade")
    val emergravidade: String,

    @ColumnInfo(name = "emerimagem")
    val emerimagem: String? = null,

    @ColumnInfo(name = "categoria")
    val categoria: String? = null,

    @ColumnInfo(name = "duracaoEstimada")
    val duracaoEstimada: Int? = null
)
