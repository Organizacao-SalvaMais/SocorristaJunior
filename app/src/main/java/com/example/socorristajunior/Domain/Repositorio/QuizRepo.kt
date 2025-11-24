package com.example.socorristajunior.Domain.Repositorio

import android.util.Log
import com.example.socorristajunior.Data.DAO.OptionDAO
import com.example.socorristajunior.Data.DAO.QuestionDAO
import com.example.socorristajunior.Data.DAO.QuizCategoryDAO
import com.example.socorristajunior.Data.DTO.QuizDto
import com.example.socorristajunior.Data.model.toEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class QuizRepo @Inject constructor(
    private val supabaseClient: SupabaseClient, // (NOVO) Cliente Supabase
    private val quizCategoryDAO: QuizCategoryDAO,
    private val questionDAO: QuestionDAO,       // (NOVO) DAO de Questões
    private val optionDAO: OptionDAO            // (NOVO) DAO de Opções
) {

    // --- Métodos de Leitura Local (Mantidos para a UI usar) ---

    // Busca categorias do banco local (Room)
    fun getCategorias() = quizCategoryDAO.getCategorias()

    // Busca quiz completo do banco local (Room)
    fun getQuizCompleto(categoriaId: Int) =
        quizCategoryDAO.getCategoriaComQuestoes(categoriaId)


    // --- (NOVO) Metodo de Sincronização com Supabase ---

    // Esta função deve ser chamada pela ViewModel (ex: no init {} ou via SwipeRefresh)
    suspend fun atualizarQuizzesDoServidor() {
        withContext(Dispatchers.IO) {
            try {
                Log.d("QuizRepo", "Iniciando sincronização com Supabase...")

                // 1. Busca os dados no Supabase (Tabela 'quizz')
                // A string "*, questaoquizz(*, alternativa(*))" faz a busca profunda (Nested Query)
                val quizzesDto = supabaseClient.postgrest["quizz"]
                    .select(columns = Columns.raw("*, questaoquizz(*, alternativa(*))"))
                    .decodeList<QuizDto>()

                Log.d("QuizRepo", "Recebidos ${quizzesDto.size} quizzes do Supabase.")

                // 2. Salva no Banco Local (Room)
                quizzesDto.forEach { quizDto ->
                    // Converte e Salva a Categoria
                    quizCategoryDAO.insert(quizDto.toEntity())

                    // Processa as Questões (se houver)
                    quizDto.questoes?.forEach { questaoDto ->
                        // Converte e Salva a Questão
                        questionDAO.insert(questaoDto.toEntity())

                        // Processa as Alternativas (se houver)
                        questaoDto.alternativas?.forEach { alternativaDto ->
                            // Converte e Salva a Opção
                            optionDAO.insert(alternativaDto.toEntity())
                        }
                    }
                }
                Log.d("QuizRepo", "Sincronização concluída com sucesso.")

            } catch (e: CancellationException) {
                // (NOVO) Ignora erro se for apenas cancelamento de corrotina (navegação)
                Log.i("QuizRepo", "Sincronização interrompida (usuário saiu da tela).")
                throw e // É boa prática relançar CancellationException em corrotinas
            } catch (e: Exception) {
                // Erros reais (sem internet, json inválido, etc)
                Log.e("QuizRepo", "Erro na sincronização: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}