package io.github.livenlearnaday.displaywithspinners.data.remote

import javax.inject.Inject


class ApiHelper @Inject constructor(private val apiService: ApiService) {

   suspend fun getSiteDataFromApi() = apiService.getSiteDataFromApi()

   suspend fun getUnitDataFromApi() = apiService.getUnitDataFromApi()



}