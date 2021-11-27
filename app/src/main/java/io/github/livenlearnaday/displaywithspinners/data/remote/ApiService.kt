package io.github.livenlearnaday.displaywithspinners.data.remote


import io.github.livenlearnaday.displaywithspinners.data.entity.SiteData
import io.github.livenlearnaday.displaywithspinners.data.entity.UnitData
import retrofit2.http.GET

interface ApiService {
    @GET("data.json")
    suspend  fun getSiteDataFromApi(): List<SiteData>

    @GET("units.json")
    suspend fun getUnitDataFromApi():List<UnitData>


}