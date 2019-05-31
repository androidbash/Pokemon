package com.harsh.pokemon.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harsh.pokemon.util.Utils.getNumber

/**
 * Created by Harsh on 29/05/2019.$
 */

@Entity(tableName = "pokemon")
data class Pokemon(
        @PrimaryKey
        var name: String,
        var url: String,
        var number: Int = getNumber(url),
        var imageUrl: String? = ""
)
