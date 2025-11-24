package com.example.socorristajunior.Data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "passo",
    foreignKeys = [ForeignKey(
        entity = Emergencia::class,
        parentColumns = ["emercodigo"],
        childColumns = ["fk_emercodigo"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("fk_emercodigo")]
)
data class Passo(
    @PrimaryKey
    @ColumnInfo(name = "pascodigo")
    val pascodigo: Int,

    @ColumnInfo(name = "pasnome")
    val pasnome: String,

    @ColumnInfo(name = "pasimagem")
    val pasimagem: String? = null,

    @ColumnInfo(name = "pasdescricao")
    val pasdescricao: String,

    @ColumnInfo(name = "pasordem")
    val pasordem: Int,

    @ColumnInfo(name = "fk_emercodigo")
    val fk_emercodigo: Int
)