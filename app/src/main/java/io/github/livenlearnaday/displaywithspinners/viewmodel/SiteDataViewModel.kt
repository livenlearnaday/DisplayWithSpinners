package io.github.livenlearnaday.displaywithspinners.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.livenlearnaday.displaywithspinners.data.entity.SiteData
import io.github.livenlearnaday.displaywithspinners.data.repository.SharedPrefHelperRepository
import io.github.livenlearnaday.displaywithspinners.data.repository.SiteDataRepository
import io.github.livenlearnaday.displaywithspinners.utils.Resource
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class SiteDataViewModel @Inject constructor(
    private val siteDataRepository: SiteDataRepository,
    private val sharedPrefHelperRepository: SharedPrefHelperRepository
) : ViewModel() {


    fun getAllSiteDataFromApi() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = siteDataRepository.getAllSiteDataFromApi()))
            } catch (exception: Exception) {
                emit(
                    Resource.error(
                        data = null,
                        message = exception.message ?: "Error Occurred."
                    )
                )
            }
        }



    fun getAllSiteDataFromDb() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = siteDataRepository.getAllSiteDataFromDb()))
            } catch (exception: Exception) {
                emit(
                    Resource.error(
                        data = null,
                        message = exception.message ?: "Error Occurred."
                    )
                )
            }
        }


    fun insertAllSiteData(siteDataList: List<SiteData>) {
        viewModelScope.launch {
            siteDataRepository.insertAllSiteData(siteDataList)
        }
    }



    fun setSitesLoaded(loaded: Boolean)= sharedPrefHelperRepository.saveSitesLoaded(loaded)



    fun isSitesLoaded() = sharedPrefHelperRepository.isSitesLoaded()




        fun clearSiteDataTable() {
            viewModelScope.launch {
                siteDataRepository.clearSiteDataTable()
            }
        }




}



