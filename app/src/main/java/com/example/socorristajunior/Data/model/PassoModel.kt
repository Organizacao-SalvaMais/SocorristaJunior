package com.example.socorristajunior.Data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "passo",
    foreignKeys = [ForeignKey(
        entity = Emergencia::class,
        parentColumns = arrayOf("emercodigo"),
        childColumns = arrayOf("pasemercodigo"),
        onDelete = ForeignKey.CASCADE
    )]
)

data class Passo(

    @PrimaryKey(autoGenerate = true)
    val pascodigo: Int,

    @ColumnInfo(name = "pasnome")
    val pasnome: String,

    @ColumnInfo(name = "pasimagem")
    val pasimagem: String,

    @ColumnInfo(name = "pasdescricao")
    val pasdescricao: String,

    @ColumnInfo(name = "pasordem")
    val pasordem: Int,

    @ColumnInfo(name = "pasemercodigo", index = true)
    val pasemercodigo: Int
) 