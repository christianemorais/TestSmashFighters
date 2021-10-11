package com.chrismorais.smashultimatefighters.data.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Universe(val objectId: String, val name: String): Parcelable
