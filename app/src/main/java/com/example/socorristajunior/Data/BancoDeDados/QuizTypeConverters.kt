package com.example.socorristajunior.Data.BancoDeDados

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Classe que contém os conversores de tipo para o Room
// O Room usará isso para salvar tipos de dados complexos (como List)
class QuizTypeConverters {

    // Cria uma instância do Gson, que é usada para serializar/desserializar JSON
    // (Assumindo que Gson está nas suas dependências do Gradle)
    private val gson = Gson()

    // Converte uma Lista de Strings (List<String>) para uma única String em formato JSON
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        // Usa o Gson para transformar a lista de opções em uma string JSON
        return gson.toJson(list)
    }

    // Converte uma String JSON de volta para uma Lista de Strings (List<String>)
    @TypeConverter
    fun toStringList(value: String): List<String> {
        // Define o tipo de dado que o Gson deve esperar (uma Lista de Strings)
        val listType = object : TypeToken<List<String>>() {}.type
        // Usa o Gson para transformar a string JSON de volta em uma Lista
        return gson.fromJson(value, listType)
    }
}
