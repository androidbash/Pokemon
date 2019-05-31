package com.harsh.pokemon.api

import android.util.Log
import com.harsh.pokemon.model.Pokemon
import com.harsh.pokemon.model.PokemonDetails
import com.harsh.pokemon.util.Utils
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by Harsh Mittal on 07/04/19.
 */
private const val TAG = "WebService"

fun getPokemonList(
        service: WebService,
        page: Int,
        offset: Int,
        onSuccess: (pokemon: List<Pokemon>, nextOffset: Int) -> Unit,
        onError: (error: String) -> Unit
) {
    Log.d(TAG, "page: $page, itemsPerPage: $offset")

    service.getPokemonList(page, offset).enqueue(

            object : Callback<ResponsePokemon> {

                override fun onFailure(call: Call<ResponsePokemon>?, t: Throwable) {
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<ResponsePokemon>?,
                        response: Response<ResponsePokemon>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val repos = response.body()?.results ?: emptyList()

                        val next = response.body()?.next
                        var nextOffset = offset
                        if (!next.isNullOrBlank()) {
                            nextOffset = Utils.getQuery(next, "offset")
                        }
                        onSuccess(repos, nextOffset)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

interface WebService {

    @GET("pokemon")
    fun getPokemonList(
            @Query("offset") offset: Int,
            @Query("limit") itemsPerPage: Int
    ): Call<ResponsePokemon>

    @GET
    fun getPokemonDetails(@Url url: String): Single<PokemonDetails>

    companion object {
        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        fun create(): WebService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WebService::class.java)
        }
    }

}