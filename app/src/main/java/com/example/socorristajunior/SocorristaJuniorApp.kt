package com.example.socorristajunior

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// A anotação @HiltAndroidApp é o que ativa o Hilt para all o aplicativo.
@HiltAndroidApp
class SocorristaJuniorApp : Application()