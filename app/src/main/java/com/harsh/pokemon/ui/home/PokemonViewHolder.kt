package com.harsh.pokemon.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.harsh.pokemon.R
import com.harsh.pokemon.data.RecylcerViewItemClickListener
import com.harsh.pokemon.model.Pokemon
import com.harsh.pokemon.util.Utils
import kotlinx.android.synthetic.main.item_row.view.*


/**
 * View Holder for a [Pokemon] RecyclerView list item.
 */
class PokemonViewHolder(view: View, itemClickListener: RecylcerViewItemClickListener) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.pokemon_name)
    private val image: AppCompatImageView = view.findViewById(R.id.pokeImage)

    private var pokemon: Pokemon? = null
    private var mCtx: Context = view.context
    private var imageUrl: String = ""

    init {
        view.setOnClickListener {
            pokemon?.let {
                it.imageUrl = imageUrl
                itemClickListener.onItemClick(it, view.pokeImage)
            }
        }
    }

    fun bind(pokemon: Pokemon?) {
        if (pokemon == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            image.setImageResource(R.mipmap.ic_launcher)
        } else {
            showRepoData(pokemon)
        }
    }

    private fun showRepoData(pokemon: Pokemon) {
        this.pokemon = pokemon
        name.text = pokemon.name
        imageUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + Utils.getNumber(pokemon.url) + ".png"

        Glide.with(mCtx).load(imageUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(image)

    }

    companion object {
        fun create(parent: ViewGroup, itemClickListener: RecylcerViewItemClickListener): PokemonViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_row, parent, false)
            return PokemonViewHolder(view, itemClickListener)
        }
    }
}