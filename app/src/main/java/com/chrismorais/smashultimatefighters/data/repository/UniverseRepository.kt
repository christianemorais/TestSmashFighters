package com.chrismorais.smashultimatefighters.data.repository

import com.chrismorais.smashultimatefighters.data.api.Service
import com.chrismorais.smashultimatefighters.data.repository.model.Universe
import retrofit2.Response

class UniverseRepository(private val service: Service) {
    suspend fun getUniverses(): Response<List<Universe>> {
       return service.universes()
    }
}