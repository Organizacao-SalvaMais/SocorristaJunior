package com.example.socorristajunior.di

import com.example.socorristajunior.Domain.Repositorio.UsuarioRepositorio
import com.example.socorristajunior.Domain.Repositorio.UsuarioRepositorioImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositorioModule {

    @Binds
    @Singleton
    abstract fun bindUsuarioRepositorio(
        usuarioRepositorioImpl: UsuarioRepositorioImpl
    ): UsuarioRepositorio
}