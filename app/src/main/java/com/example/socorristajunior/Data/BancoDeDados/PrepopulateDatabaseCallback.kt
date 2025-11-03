package com.example.socorristajunior.Data.BancoDeDados

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.socorristajunior.Data.model.ApiResponse
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Data.model.toEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider

class PrepopulateDatabaseCallback(
    private val context: Context,
    // Usamos Provider para evitar uma dependência circular no Hilt
    private val database: Provider<AppDatabase>
) : RoomDatabase.Callback() {

    // Este metodo é chamado pelo Room APENAS na primeira vez que o banco de dados é criado.
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // Lançamos uma coroutine para fazer o trabalho pesado (ler arquivo, inserir no banco)
        // em uma thread de segundo plano (Dispatchers.IO).
        CoroutineScope(Dispatchers.IO).launch {
            prepopulateDatabase()
        }
    }

    private suspend fun prepopulateDatabase() {
        try {
            val inputStream = context.assets.open("emergencias.json") // Seu novo JSON
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            // Decodifica o JSON para a classe 'ApiResponse'
            val type = object : TypeToken<ApiResponse>() {}.type
            val apiResponse: ApiResponse = Gson().fromJson(jsonString, type)

            // Insere a lista de Entidades no banco de dados
            val emergenciaDao = database.get().emergenciaDAO()
            val passoDAO = database.get().passoDAO()

            // Lista mutável para armazenar todos os passos de todas as emergências.
            val todosOsPassosParaInserir = mutableListOf<Passo>()

            apiResponse.data.forEach { emergenciaJson ->
                val emergenciaEntity = emergenciaJson.toEntity()

                val novoEmercodigo = emergenciaDao.insertEmergencia(emergenciaEntity)

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

            passoDAO.insertAllPassos(todosOsPassosParaInserir)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}