package com.example.socorristajunior.Data.DTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInteractionDto(
    @SerialName("fk_usucodigo") val userId: Int,
    @SerialName("fk_emercodigo") val emergencyId: Int,
    @SerialName("favorito") val isFavorite: Boolean,
    @SerialName("visualizado") val isViewed: Boolean
)