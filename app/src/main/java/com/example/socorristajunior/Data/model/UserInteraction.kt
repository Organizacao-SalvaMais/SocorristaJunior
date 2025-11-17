package com.example.socorristajunior.Data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "user_interaction",
    primaryKeys = ["userId", "emergencyId"], // Chave composta
    foreignKeys = [
        ForeignKey(
            entity = Emergencia::class,
            parentColumns = ["emercodigo"],
            childColumns = ["emergencyId"],
            onDelete = ForeignKey.CASCADE
        )
        // Adicione a FK de UserEntity se você tiver essa tabela local sincronizada
    ],
    indices = [Index("emergencyId")]
)
data class UserInteraction(
    val userId: Int, // O ID numérico do usuário (usucodigo)
    val emergencyId: Int,
    val isFavorite: Boolean = false,
    val isViewed: Boolean = false
)