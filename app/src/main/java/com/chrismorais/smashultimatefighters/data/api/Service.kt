package com.chrismorais.smashultimatefighters.data.api

import com.chrismorais.smashultimatefighters.data.repository.model.Fighter
import com.chrismorais.smashultimatefighters.data.repository.model.Universe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("universes")
    suspend fun universes(): Response<List<Universe>>


    @GET("fighters")
    suspend fun fighters(@Query("universe") universe: String? = null): Response<List<Fighter>>
}
