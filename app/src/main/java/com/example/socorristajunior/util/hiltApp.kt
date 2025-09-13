package com.example.socorristajunior.util

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Todos os apps que usam o Hilt precisam conter uma classe Application anotada com @HiltAndroidApp.
// O @HiltAndroidApp aciona a geração de código do Hilt, incluindo uma classe base para seu aplicativo que serve como contêiner de dependências no nível do app.
@HiltAndroidApp
class hiltApp : Application()