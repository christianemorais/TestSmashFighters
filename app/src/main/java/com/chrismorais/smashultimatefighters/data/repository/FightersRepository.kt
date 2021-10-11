package com.chrismorais.smashultimatefighters.data.repository

import com.chrismorais.smashultimatefighters.data.api.Service
import com.chrismorais.smashultimatefighters.data.repository.model.Fighter
import retrofit2.Response

class FightersRepository(private val service: Service) {
    suspend fun getAllFighters(): Response<List<Fighter>> {
        return service.fighters()
    }

    suspend fun getFightersByUniverse(universeName: String): Response<List<Fighter>> {
        return service.fighters(universeName)
    }
}