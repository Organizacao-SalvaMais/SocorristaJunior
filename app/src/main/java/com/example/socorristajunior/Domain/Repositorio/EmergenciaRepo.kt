package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DTO.EmergenciaApiDto
import com.example.socorristajunior.Data.model.Emergencia
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import io.github.jan.supabase.postgrest.query.Columns
import android.util.Log
import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Data.model.Passo

class EmergenciaRepo @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val emergenciaDAO: EmergenciaDAO,
    private val passoDAO: PassoDAO
) {
    // --- Funções do Banco de Dados Local (Room) ---

    // Esta função agora será a "fonte da verdade" para a UI
    fun getAllEmergencias(): Flow<List<Emergencia>> {
        return emergenciaDAO.getAllEmergencias()
    }

    suspend fun getEmergenciaById(id: Int): Emergencia? {
        return emergenciaDAO.getEmergenciaById(id)
    }

    // --- Funções do Supabase (Remoto) ---

    /*
      Busca todas as emergências do Supabase, incluindo os dados
      aninhados das tabelas 'gravidade' e 'fontes'.
     */

    private suspend fun fetchAllEmergenciasFromApi(): List<EmergenciaApiDto> {
        val colunas = Columns.raw("*, gravidade(id, gravnome), fontes(*), passos(*)")

        return supabaseClient.postgrest["emergencia"]
            .select(
                columns = colunas
            )
            .decodeList<EmergenciaApiDto>()
    }

    /*
      Função de sincronização.
      1. Busca dados frescos do Supabase.
      2. Mapeia os DTOs da API para o modelo de banco de dados local (Emergencia).
      3. Insere todos os dados novos no banco de dados Room.
     */

    suspend fun syncEmergenciasFromSupabase() {
        // 1. Buscar dados da API
        val emergenciasApi = fetchAllEmergenciasFromApi()

        Log.d("SupabaseSync", "Total de emergências baixadas do Supabase: ${emergenciasApi.size}")

        val todosOsPassos = mutableListOf<Passo>()

        // 2. Mapear DTO da API para o Modelo local 'Emergencia'
        val emergencies = emergenciasApi.map { dto ->

            dto.passos.forEach { passoDto ->
                todosOsPassos.add(
                    Passo(
                        // O 'pascodigo' (PK) não vem do DTO, o Room irá gerá-lo ou substituí-lo
                        // Vamos usar o 'id' do DTO como chave primária para consistência
                        pascodigo = passoDto.id, // Usa o ID do Supabase como Chave Primária
                        pasnome = passoDto.pasnome,
                        pasimagem = passoDto.pasimagem,
                        pasdescricao = passoDto.pasdescricao,
                        pasordem = passoDto.pasordem,
                        pasemercodigo = passoDto.emer_id // Chave estrangeira
                    )
                )
            }

            Emergencia(
                emercodigo = dto.id,
                emernome = dto.emernome,
                emerdesc = dto.emerdesc,
                // Agora podemos pegar o nome da gravidade do objeto aninhado!
                emergravidade = dto.gravidade?.gravnome ?: "Não definida",
                emerimagem = dto.emerimagem,
                // Esses campos não estão no seu schema do Supabase,
                // então continuamos a preenchê-los como nulo ou padrão.
                categoria = null,
                duracaoEstimada = null
            )
        }

        // 3. Salvar os dados mapeados no banco de dados local
        // (Isso irá sobrescrever os dados antigos com os novos)
        emergenciaDAO.insertAllEmergencias(emergencies)
        passoDAO.insertAllPassos(todosOsPassos)

        Log.d("SupabaseSync", "Total de passos baixados do Supabase: ${todosOsPassos.size}")

    }

    // --- Funções antigas do DAO (mantidas para referência, se necessário) ---

    fun searchEmergencias(query: String): Flow<List<Emergencia>> {
        return emergenciaDAO.searchEmergencias(query)
    }

    fun getEmergenciasPorGravidade(gravidade: String): Flow<List<Emergencia>> {
        return emergenciaDAO.getEmergenciasPorGravidade(gravidade)
    }

    fun getEmergenciasPorCategoria(categoria: String): Flow<List<Emergencia>> {
        return emergenciaDAO.getEmergenciasPorCategoria(categoria)
    }

    suspend fun insertEmergencia(emergencia: Emergencia) {
        emergenciaDAO.insertEmergencia(emergencia)
    }

    suspend fun insertAllEmergencias(emergencias: List<Emergencia>) {
        emergenciaDAO.insertAllEmergencias(emergencias)
    }

    suspend fun updateEmergencia(emergencia: Emergencia) {
        emergenciaDAO.updateEmergencia(emergencia)
    }

    suspend fun getTotalEmergencias(): Int {
        return emergenciaDAO.getTotalEmergencias()
    }

}