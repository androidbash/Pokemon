package com.harsh.pokemon.db

import android.util.Log
import androidx.lifecycle.LiveData
import com.harsh.pokemon.model.Pokemon
import java.util.concurrent.Executor

/**
 * Created by Harsh Mittal on 07/04/19.
 */


/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class PokemonLocalCache(
        private val pokemonDao: PokemonDao,
        private val ioExecutor: Executor
) {

    /**
     * Insert a list of pokemon in the database, on a background thread.
     */
    fun insert(users: List<Pokemon>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("PokemonLocalCache", "inserting ${users.size} users")
            pokemonDao.insert(users)
            insertFinished()
        }
    }

    /**
     * Insert a list of pokemon in the database, on a background thread.
     */
    fun clearAll() {
        ioExecutor.execute {
            pokemonDao.clearAll()
        }
    }


    /**
     * Request a LiveData<List<Pokemon>> from the Dao,
     */
    fun getPokemonList(): LiveData<List<Pokemon>> {
        return pokemonDao.getPokemonList()
    }
}