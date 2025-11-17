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
import com.example.socorristajunior.Data.DTO.UserInteractionDto
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Data.model.UserInteraction

class EmergenciaRepo @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val emergenciaDAO: EmergenciaDAO,
    private val passoDAO: PassoDAO,
    private val interactionDAO: com.example.socorristajunior.Data.DAO.UserInteractionDAO
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
        val colunas = Columns.raw("*, gravidade(*), fontes(*), passos(*)")

        return supabaseClient.postgrest["emergencia"]
            .select(columns = colunas)
            .decodeList<EmergenciaApiDto>()
    }

    /*
      Função de sincronização.
      1. Busca dados frescos do Supabase.
      2. Mapeia os DTOs da API para o modelo de banco de dados local (Emergencia).
      3. Insere todos os dados novos no banco de dados Room.
     */

    suspend fun syncEmergenciasFromSupabase() {
        try {
            // 1. Buscar dados da API
            val emergenciasApi = fetchAllEmergenciasFromApi()
            Log.d("SupabaseSync", "Total de emergências baixadas: ${emergenciasApi.size}")

            val todasEmergenciasRoom = mutableListOf<Emergencia>()
            val todosOsPassosRoom = mutableListOf<Passo>()

            // 2. Mapear DTOs para Entidades Room
            for (dto in emergenciasApi) {
                // Mapeia a Emergencia
                todasEmergenciasRoom.add(
                    Emergencia(
                        emercodigo = dto.emercodigo,
                        emernome = dto.emernome,
                        emerdesc = dto.emerdesc,
                        emerimagem = dto.emerimagem,
                        gravidadeNome = dto.gravidade?.gravnome ?: "Não definida",
                        gravidadeCor = dto.gravidade?.gravicor,
                        fonteNome = dto.fontes?.fonnome ?: "Não definida",
                        fonteUrl = dto.fontes?.url
                    )
                )

                // Mapeia os Passos dessa Emergencia
                dto.passos.forEach { passoDto ->
                    todosOsPassosRoom.add(
                        Passo(
                            pascodigo = passoDto.pascodigo,
                            pasnome = passoDto.pasnome,
                            pasimagem = passoDto.pasimagem,
                            pasdescricao = passoDto.pasdescricao,
                            pasordem = passoDto.pasordem,
                            fk_emercodigo = passoDto.fk_emercodigo
                        )
                    )
                }
            }

            // 3. Salvar os dados mapeados no banco de dados local
            // O OnConflictStrategy.REPLACE irá atualizar dados existentes
            emergenciaDAO.insertAllEmergencias(todasEmergenciasRoom)
            passoDAO.insertAllPassos(todosOsPassosRoom)

            Log.d("SupabaseSync", "Sincronização concluída. ${todosOsPassosRoom.size} passos salvos.")

        } catch (e: Exception) {
            // É crucial tratar erros de rede ou de parsing
            Log.e("SupabaseSync", "Falha ao sincronizar dados: ${e.message}")
            // Você pode querer re-lançar o erro ou
            // notificar a UI de alguma forma
        }
    }

    // --- Funções antigas do DAO (mantidas para referência, se necessário) ---

    fun searchEmergencias(query: String): Flow<List<Emergencia>> {
        return emergenciaDAO.searchEmergencias(query)
    }

    fun getEmergenciasPorGravidade(gravidade: String): Flow<List<Emergencia>> {
        // Note que o DAO já foi atualizado para "gravidade_nome"
        return emergenciaDAO.getEmergenciasPorGravidade(gravidade)
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

    // --- Gestão de Interações (Favoritos/Visualizados) ---

    /*
     * Alterna o status de favorito.
     * 1. Atualiza Localmente (Instantâneo para o usuário)
     * 2. Envia para o Supabase (Background)
     */
    suspend fun toggleFavorite(userId: Int, emergencyId: Int, currentStatus: Boolean) {
        val newStatus = !currentStatus

        // 1. Recupera ou cria a interação local
        val interaction = UserInteraction(
            userId = userId,
            emergencyId = emergencyId,
            isFavorite = newStatus,
            isViewed = true // Se favoritou, assume que visualizou
        )

        // Salva no Room
        interactionDAO.saveInteraction(interaction)

        // 2. Envia para o Supabase (Upsert)
        try {
            val dto = UserInteractionDto(userId, emergencyId, newStatus, true)
            supabaseClient.postgrest["usuario_interacao"].upsert(dto)
        } catch (e: Exception) {
            Log.e("Repo", "Erro ao salvar favorito na nuvem: ${e.message}")
            // Em um app real, você agendaria uma sincronização com WorkManager aqui
        }
    }

    /*
     * Marca como visualizado ao abrir a tela
     */
    suspend fun markAsViewed(userId: Int, emergencyId: Int) {
        // Lógica similar, mas apenas setando isViewed = true
        // Preserva o estado de favorito atual se existir
        // ... (implementação similar ao toggleFavorite)
    }

    fun getInteraction(userId: Int, emerId: Int): Flow<UserInteraction?> {
        return interactionDAO.getInteractionFlow(userId, emerId)
    }

}