package com.harsh.pokemon.data

import android.widget.ImageView
import com.harsh.pokemon.model.Pokemon

interface RecylcerViewItemClickListener {
    fun onItemClick(pokemon: Pokemon, shareImageView: ImageView)
}