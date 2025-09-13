package com.example.socorristajunior.Data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "emergencia",
    indices = [

        Index("emernome", unique = true)

    ]
)

data class Emergencia(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "emercodigo")
    val emercodigo: Int = 0,

    @ColumnInfo(name = "emernome")
    val emernome: String,

    @ColumnInfo(name = "emerdesc")
    val emerdesc: String,

    @ColumnInfo(name = "emergravidade")
    val emeremergravidade: String,

    @ColumnInfo(name = "emerimagem")
    val emerimagem: String
)