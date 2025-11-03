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
        childColumns = ["pasemercodigo"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("pasemercodigo")]
)
data class Passo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pascodigo")
    val pascodigo: Int = 0,

    @ColumnInfo(name = "pasnome")
    val pasnome: String,

    @ColumnInfo(name = "pasimagem")
    val pasimagem: String? = null,

    @ColumnInfo(name = "pasdescricao")
    val pasdescricao: String,

    @ColumnInfo(name = "pasordem")
    val pasordem: Int,

    @ColumnInfo(name = "pasemercodigo")
    val pasemercodigo: Int
)