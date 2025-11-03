package com.example.socorristajunior.Data.Services

import retrofit2.http.Query
import com.example.socorristajunior.Data.DTO.EmergenciaApiDto
import com.example.socorristajunior.Data.DTO.PassoApiDto
import retrofit2.http.GET


interface EmergenciaApiService {
    @GET("emergencia")
    suspend fun getEmergencias(): List<EmergenciaApiDto>

    @GET("passos")
    suspend fun getPassosByEmergencia(@Query("emer_id") emerId: Long): List<PassoApiDto>
}
