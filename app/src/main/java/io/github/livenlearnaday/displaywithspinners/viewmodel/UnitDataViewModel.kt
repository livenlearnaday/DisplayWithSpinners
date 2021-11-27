package io.github.livenlearnaday.displaywithspinners.viewmodel

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.livenlearnaday.displaywithspinners.data.entity.UnitData
import io.github.livenlearnaday.displaywithspinners.data.repository.SharedPrefHelperRepository
import io.github.livenlearnaday.displaywithspinners.data.repository.UnitDataRepository
import io.github.livenlearnaday.displaywithspinners.utils.Resource
import kotlinx.coroutines.*
import java.util.stream.Collectors
import javax.inject.Inject


@HiltViewModel
class UnitDataViewModel @Inject constructor(
    private val unitDataRepository: UnitDataRepository,
    private val sharedPrefHelperRepository: SharedPrefHelperRepository
) : ViewModel() {


    fun getAllUnitDataFromApi() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = unitDataRepository.getAllUnitDataFromApi()))
            } catch (exception: Exception) {
                emit(
                    Resource.error(
                        data = null,
                        message = exception.message ?: "Error Occurred."
                    )
                )
            }
        }


    fun getAllUnitDataFromDb() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = unitDataRepository.getAllUnitDataFromDb()))
            } catch (exception: Exception) {
                emit(
                    Resource.error(
                        data = null,
                        message = exception.message ?: "Error Occurred."
                    )
                )
            }
        }



    fun getAllNotLockedUnitDataFromDb() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = unitDataRepository.getAllNotLockedUnitDataFromDb()))
            } catch (exception: Exception) {
                emit(
                    Resource.error(
                        data = null,
                        message = exception.message ?: "Error Occurred."
                    )
                )
            }
        }



    fun getAllBaseUnitDataFromDb() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = unitDataRepository.getAllBaseUnitDataFromDb()))
            } catch (exception: Exception) {
                emit(
                    Resource.error(
                        data = null,
                        message = exception.message ?: "Error Occurred."
                    )
                )
            }
        }


    fun setUnitsLoaded(unitLoaded: Boolean) = sharedPrefHelperRepository.saveUnitsLoaded(unitLoaded)




    fun isUnitsLoaded() = sharedPrefHelperRepository.isUnitsLoaded()



    fun insertAllUnitData(unitDataList: List<UnitData>) = viewModelScope.launch {
        unitDataRepository.insertAllUnitData(unitDataList)
    }

    fun insertUnitData(item: UnitData) = viewModelScope.launch {
        unitDataRepository.insertUnitData(item)
    }



        fun getMeasurementSystemList(unitDataList: List<UnitData>): List<String> =
            unitDataRepository.getMeasurementSystemList(unitDataList)

        fun getCategoryUnitListBySystem(
            unitDataList: List<UnitData>,
            system: String,
            category: String
        ): List<String> = unitDataRepository.getCategoryUnitListBySystem(unitDataList, system, category)



        fun getAllBaseUnitObjects(unitDataList: List<UnitData>): List<UnitData> =
            unitDataRepository.getAllBaseUnitObjects(unitDataList)

        fun getBaseUnitObjectForGivenCategory(
            unitDataList: List<UnitData>,
            category: String
        ): UnitData = unitDataRepository.getBaseUnitObjectForGivenCategory(unitDataList, category)


        fun unitSystemExistedInDb(unitDataList: List<UnitData>, system: String): Boolean =
            unitDataRepository.unitSystemExistedInDb(unitDataList, system)


        fun deleteUnitDataBySystemCategoryName(system:String, category: String, name: String) = viewModelScope.launch {
            unitDataRepository.deleteUnitDataBySystemCategoryName(system, category,name)
        }




        fun clearUnitDataTable() {
            viewModelScope.launch {
                unitDataRepository.clearUnitDataTable()
            }
        }

        fun getAllCategoryStringList(unitDataList: List<UnitData>): List<String> =
            unitDataRepository.getAllCategoryStringList(unitDataList)




}



