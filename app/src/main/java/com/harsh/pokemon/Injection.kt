package com.harsh.pokemon

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.harsh.pokemon.api.WebService
import com.harsh.pokemon.data.WebRepository
import com.harsh.pokemon.db.AppDatabase
import com.harsh.pokemon.db.PokemonLocalCache
import com.harsh.pokemon.ui.home.ViewModelFactory
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [PokemonLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): PokemonLocalCache {
        val database = AppDatabase.getInstance(context)
        return PokemonLocalCache(database.pokemonDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [WebRepository] based on the [GithubService] and a
     * [PokemonLocalCache]
     */
    private fun provideWebRepository(context: Context): WebRepository {
        return WebRepository(WebService.create(), provideCache(context))
    }


    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideWebRepository(context))
    }


}