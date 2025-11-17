package com.example.socorristajunior.Data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergencia")
data class Emergencia(
    @PrimaryKey
    @ColumnInfo(name = "emercodigo")
    val emercodigo: Int,

    @ColumnInfo(name = "emernome")
    val emernome: String,

    @ColumnInfo(name = "emerdesc")
    val emerdesc: String,

    @ColumnInfo(name = "emerimagem")
    val emerimagem: String? = null,

    @ColumnInfo(name = "gravidade_nome")
    val gravidadeNome: String,

    @ColumnInfo(name = "gravidade_cor")
    val gravidadeCor: String? = null,

    @ColumnInfo(name = "fonte_nome")
    val fonteNome: String,

    @ColumnInfo(name = "fonte_url")
    val fonteUrl: String? = null
)