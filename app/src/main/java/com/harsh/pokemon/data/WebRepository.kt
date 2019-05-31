package com.harsh.pokemon.data

import androidx.lifecycle.MutableLiveData
import com.harsh.pokemon.api.WebService
import com.harsh.pokemon.api.getPokemonList
import com.harsh.pokemon.db.PokemonLocalCache
import com.harsh.pokemon.model.PokemonResult

/**
 * Created by Harsh Mittal on 07/04/19.
 */

/**
 * Repository class that works with local and remote data sources.
 */
class WebRepository(
        private val service: WebService,
        private val cache: PokemonLocalCache
) {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 0

    // LiveData of network errors.
    private val networkErrors = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false


    fun responsePokemon(): PokemonResult {

        cache.clearAll()
        lastRequestedPage = 0
        requestAndSaveData()

        // Get data from the local cache
        val data = cache.getPokemonList()

        return PokemonResult(data)
    }

    fun requestMore() {
        requestAndSaveData()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) return

        if (lastRequestedPage == -1) {
            return
        }

        isRequestInProgress = true
        getPokemonList(service, lastRequestedPage, NETWORK_PAGE_SIZE, { list, nextOffset ->
            cache.insert(list) {
                if (lastRequestedPage == nextOffset) {
                    lastRequestedPage = -1
                }
                lastRequestedPage = nextOffset
                isRequestInProgress = false
            }
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}