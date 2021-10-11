package com.chrismorais.smashultimatefighters.data.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fighter(
    val objectID: String,
    val name: String,
    val universe: String,
    val price: String,
    val rate: Int,
    val downloads: String,
    val description: String,
    val imageURL: String
) : Parcelable
