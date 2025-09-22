// Define o pacote para o módulo de injeção de dependência.
package com.example.socorristajunior.di

// Imports necessários do Android, Room, Hilt e das suas classes de dados.
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.socorristajunior.Data.BancoDeDados.AppDatabase
import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

// @Module informa ao Hilt que este objeto contém definições de como criar dependências.
@Module
// @InstallIn(SingletonComponent::class) significa que as dependências criadas aqui viverão por todo o ciclo de vida do app.
@InstallIn(SingletonComponent::class)
object AppModule {

    // 1. PROVIDER PARA O BANCO DE DADOS (DATABASE)
    // @Provides ensina o Hilt a criar uma instância do AppDatabase.
    @Provides
    // @Singleton garante que apenas uma única instância do banco de dados seja criada no app inteiro.
    @Singleton
    fun provideAppDatabase(
        // @ApplicationContext injeta o contexto da aplicação.
        @ApplicationContext context: Context,
        // Usamos Provider<> para injetar os DAOs de forma segura no Callback, evitando um "circular dependency" (dependência circular).
        emergenciaDaoProvider: Provider<EmergenciaDAO>,
        passoDaoProvider: Provider<PassoDAO>
    ): AppDatabase {
        // Constrói a instância do banco de dados Room.
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "socorrista_junior_db" // Nome do arquivo do banco de dados.
        )
            // Adiciona um "callback" que será executado em eventos do banco, como sua criação.
            .addCallback(object : RoomDatabase.Callback() {
                // Este metodo é chamado apenas uma vez, quando o banco de dados é criado pela primeira vez.
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Lança uma "coroutine" para executar a inserção de dados fora da "main thread" (thread principal), para não travar a UI.
                    CoroutineScope(Dispatchers.IO).launch {
                        // Chama nossa função para preencher o banco com os dados iniciais.
                        preencherDadosIniciais(
                            emergenciaDaoProvider.get(), // .get() obtém a instância real do DAO.
                            passoDaoProvider.get()
                        )
                    }
                }
            })
            .build() // Finaliza a construção do banco.
    }

    // 2. PROVIDERS PARA OS DAOs
    // Ensina o Hilt a criar um EmergenciaDAO. Ele sabe que precisa de um AppDatabase, que é provido pela função acima.
    @Provides
    @Singleton
    fun provideEmergenciaDao(appDatabase: AppDatabase): EmergenciaDAO {
        return appDatabase.EmergenciaDAO()
    }

    // Ensina o Hilt a criar um PassoDAO.
    @Provides
    @Singleton
    fun providePassoDao(appDatabase: AppDatabase): PassoDAO {
        return appDatabase.PassoDAO()
    }

    // 3. PROVIDERS PARA OS REPOSITÓRIOS (REPOSITORIES) - ESTA PARTE É NOVA E ESSENCIAL
    // Ensina o Hilt a criar um EmergenciaRepo. Ele precisa de um EmergenciaDAO, que já sabe como criar.
    @Provides
    @Singleton
    fun provideEmergenciaRepo(emergenciaDAO: EmergenciaDAO): EmergenciaRepo {
        return EmergenciaRepo(emergenciaDAO)
    }

    // Ensina o Hilt a criar um PassoRepo.
    @Provides
    @Singleton
    fun providePassoRepo(passoDAO: PassoDAO): PassoRepo {
        return PassoRepo(passoDAO)
    }
}

// 4. FUNÇÃO PARA INSERIR OS DADOS INICIAIS
// Esta função é chamada pelo "callback" para popular o banco de dados.
private suspend fun preencherDadosIniciais(emergenciaDao: EmergenciaDAO, passoDao: PassoDAO) {
    // Cria uma lista de objetos EmergenciaModel para inserir.
    val emergencias = listOf(
        Emergencia(emercodigo = 1, emernome = "Queimaduras", emerdesc = "Lesão na pele causada por calor.", emeremergravidade = "Moderada", emerimagem = "fire"),
        Emergencia(emercodigo = 2, emernome = "Engasgo", emerdesc = "Obstrução das vias aéreas.", emeremergravidade = "Alta", emerimagem = "choke"),
        Emergencia(emercodigo = 3, emernome = "Cortes e Ferimentos", emerdesc = "Ferimento que atravessa a pele.", emeremergravidade = "Baixa", emerimagem = "cut")
    )
    // Insere todas as emergências no banco. O '*' antes da lista a transforma em "varargs".
    emergenciaDao.insertAll(*emergencias.toTypedArray())

    // Cria uma lista de passos para a emergência de Queimaduras (código 1).
    val passosQueimadura = listOf(
        Passo(pascodigo = 1, pasemercodigo = 1, pasordem = 1, pasnome = "Resfrie a Área", pasdescricao = "Coloque a área queimada sob água fria corrente por pelo menos 10 minutos. Não use gelo.", pasimagem = ""),
        Passo(pascodigo = 2, pasemercodigo = 1, pasordem = 2, pasnome = "Cubra a Queimadura", pasdescricao = "Cubra a área com um pano limpo e úmido ou um curativo estéril. Não estoure bolhas.", pasimagem = ""),
        Passo(pascodigo = 3, pasemercodigo = 1, pasordem = 3, pasnome = "Procure Ajuda Médica", pasdescricao = "Para queimaduras graves, grandes ou em áreas sensíveis, procure um médico imediatamente.", pasimagem = "")
    )
    // Insere os passos no banco.
    passoDao.insertAll(*passosQueimadura.toTypedArray())

    // Cria e insere os passos para a emergência de Engasgo (código 2).
    val passosEngasgo = listOf(
        Passo(pascodigo = 4, pasemercodigo = 2, pasordem = 1, pasnome = "Estimule a Tosse", pasdescricao = "Peça para a pessoa tossir com força. A tosse é a forma mais eficaz de expelir o objeto.", pasimagem = ""),
        Passo(pascodigo = 5, pasemercodigo = 2, pasordem = 2, pasnome = "Manobra de Heimlich", pasdescricao = "Se a pessoa não consegue tossir, posicione-se atrás dela e realize compressões abdominais (Manobra de Heimlich).", pasimagem = ""),
        Passo(pascodigo = 6, pasemercodigo = 2, pasordem = 3, pasnome = "Ligue para a Emergência", pasdescricao = "Se a pessoa desmaiar ou a obstrução não for resolvida, ligue para 192 (SAMU) imediatamente e inicie a reanimação cardiopulmonar.", pasimagem = "")
    )
    passoDao.insertAll(*passosEngasgo.toTypedArray())

    // Você pode adicionar mais passos para outras emergências aqui...
}