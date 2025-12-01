package com.example.socorristajunior.Data.Services

import retrofit2.http.Query
import com.example.socorristajunior.Data.DTO.EmergenciaApiDto
import com.example.socorristajunior.Data.DTO.PassoApiDto
import retrofit2.http.GET


interface EmergenciaApiService {
    @GET("emergencia")
    suspend fun getEmergencias(
        @Query("select") select: String = "*,fk_gravcodigo(*),fk_foncodigo(*)"
    ): List<EmergenciaApiDto>

    @GET("passos")
    suspend fun getPassosByEmergencia(
        @Query("fk_emercodigo") emerIdFilter: String
    ): List<PassoApiDto>
}
