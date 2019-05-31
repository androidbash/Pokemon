package com.harsh.pokemon.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harsh.pokemon.data.RecylcerViewItemClickListener
import com.harsh.pokemon.model.Pokemon

/**
 * Adapter for the list of repositories.
 */
class PokemonAdapter(private val itemClickListener: RecylcerViewItemClickListener) : ListAdapter<Pokemon, RecyclerView.ViewHolder>(DATA_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PokemonViewHolder.create(parent, itemClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokeItem = getItem(position)
        if (pokeItem != null) {
            (holder as PokemonViewHolder).bind(pokeItem)
        }
    }

    companion object {
        private val DATA_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                    oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                    oldItem == newItem
        }
    }
}