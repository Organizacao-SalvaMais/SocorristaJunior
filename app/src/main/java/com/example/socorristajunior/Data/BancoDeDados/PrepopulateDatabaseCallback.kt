package com.example.socorristajunior.Data.BancoDeDados

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.socorristajunior.Data.DAO.OptionDAO
import com.example.socorristajunior.Data.DAO.QuestionDAO
import com.example.socorristajunior.Data.DAO.QuizCategoryDAO
import com.example.socorristajunior.Data.model.ApiResponse
import com.example.socorristajunior.Data.model.Option
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Data.model.Question
import com.example.socorristajunior.Data.model.QuizApiResponse
import com.example.socorristajunior.Data.model.QuizCategory
import com.example.socorristajunior.Data.model.toEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import javax.inject.Provider

class PrepopulateDatabaseCallback(
    private val context: Context,
    // A proxima linha esta comentada pois não precisamos mais chamar do JSON
    // private val dbProvider: Provider<AppDatabase>,
    private val quizCategoryDAO: Provider<QuizCategoryDAO>,
    private val questionDAO: Provider<QuestionDAO>,
    private val optionDAO: Provider<OptionDAO>
) : RoomDatabase.Callback() {

    // Define um escopo de corrotina para rodar o prepopulate fora da thread principal
    private val applicationScope = CoroutineScope(Dispatchers.IO)

    // Chamado quando o banco de dados é criado pela primeira vez
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // Lança a corrotina para popular AMBOS os JSONs
        applicationScope.launch {
            // JSON de Emergencias
            // prepopulateEmergencias() // Popula as emergências
            // JSON de Quizzes
            prepopulateQuizzes()     // Popula os quizzes
        }
    }
    // Não precisamos mais da função abaixo.
/*
    private suspend fun prepopulateEmergencias() {
        try {
            // Abre o arquivo de emergências
            val inputStream = context.assets.open("emergencias.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            // Decodifica o JSON para a classe 'ApiResponse'
            val type = object : TypeToken<ApiResponse>() {}.type
            val apiResponse: ApiResponse = Gson().fromJson(jsonString, type)

            // Obtém os DAOs de Emergência a partir do Provider do banco
            val emergenciaDao = dbProvider.get().emergenciaDAO()
            val passoDAO = dbProvider.get().passoDAO()

            val todosOsPassosParaInserir = mutableListOf<Passo>()

            apiResponse.data.forEach { emergenciaJson ->
                val emergenciaEntity = emergenciaJson.toEntity()
                // Insere a emergência e pega seu novo ID
                val novoEmercodigo = emergenciaDao.insertEmergencia(emergenciaEntity)

                // Mapeia os passos para esta emergência
                val passosDaEmergencia = emergenciaJson.passos?.map { passoJson ->
                    Passo(
                        pasnome = passoJson.pasnome,
                        pasimagem = passoJson.pasimagem,
                        pasdescricao = passoJson.pasdescricao,
                        pasordem = passoJson.pasordem,
                        pasemercodigo = novoEmercodigo.toInt()
                    )
                }

                if (!passosDaEmergencia.isNullOrEmpty()) {
                    todosOsPassosParaInserir.addAll(passosDaEmergencia)
                }
            }
            // Insere todos os passos de uma vez
            passoDAO.insertAllPassos(todosOsPassosParaInserir)

        } catch (e: Exception) {
            e.printStackTrace() // Loga erro se o arquivo não for encontrado
        }
    }
*/
    private suspend fun prepopulateQuizzes() {
        try {
            // 1. ABRIR O ARQUIVO JSON
            val inputStream = context.assets.open("quiz.json")
            val reader = InputStreamReader(inputStream)

            // 2. PARSEAR O JSON USANDO GSON
            val apiResponse = Gson().fromJson(reader, QuizApiResponse::class.java)

            // 3. MAPEAR DTOS PARA ENTIDADES
            val categorias = mutableListOf<QuizCategory>()
            val questoes = mutableListOf<Question>()
            val opcoes = mutableListOf<Option>()

            apiResponse.data.forEach { categoriaDto ->
                // Mapeia a categoria e adiciona à lista
                categorias.add(categoriaDto.toEntity())

                categoriaDto.questoes.forEach { questaoDto ->
                    // Mapeia a questão (passando o ID da categoria) e adiciona
                    questoes.add(questaoDto.toEntity(categoriaId = categoriaDto.qzcodigo))

                    questaoDto.opcoes.forEach { opcaoDto ->
                        // Mapeia a opção (passando o ID da questão) e adiciona
                        opcoes.add(opcaoDto.toEntity(questaoId = questaoDto.qtcodigo))
                    }
                }
            }

            // 4. INSERIR NO BANCO DE DADOS
            // Obtém os DAOs que foram injetados no construtor
            val catDao = quizCategoryDAO.get()
            val qstDao = questionDAO.get()
            val optDao = optionDAO.get()

            // Insere todas as listas no banco
            catDao.insertCategorias(categorias)
            qstDao.insertQuestoes(questoes)
            optDao.insertOpcoes(opcoes)

        } catch (e: Exception) {
            e.printStackTrace() // Loga erro se o arquivo não for encontrado
        }
    }
}