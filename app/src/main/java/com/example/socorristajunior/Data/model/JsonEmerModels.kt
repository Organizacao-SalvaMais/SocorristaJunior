package com.example.socorristajunior.Data.model

import com.google.gson.annotations.SerializedName

// Classe DTO para puxar do Json

data class ApiResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<EmergenciaJson>
)

data class EmergenciaJson(
    @SerializedName("emernome")
    val emernome: String,

    @SerializedName("emerdesc")
    val emerdesc: String,

    @SerializedName("emergravicodigo")
    val emergravicodigo: Int,

    @SerializedName("emerimagem")
    val emerimagem: String?, // Já definido como nulo para corresponder ao JSON

    @SerializedName("emerfoncodigo")
    val emerfoncodigo: Int,

    @SerializedName("passos")
    val passos: List<PassoJson>? = emptyList()
)

data class PassoJson(
    @SerializedName("pasnome")
    val pasnome: String,

    @SerializedName("pasimagem")
    val pasimagem: String?,

    @SerializedName("pasdescricao")
    val pasdescricao: String,

    @SerializedName("pasordem")
    val pasordem: Int
)


