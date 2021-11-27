package io.github.livenlearnaday.displaywithspinners.data.repository

import io.github.livenlearnaday.displaywithspinners.data.entity.UnitData
import io.github.livenlearnaday.displaywithspinners.data.entity.UnitDataDao
import io.github.livenlearnaday.displaywithspinners.data.remote.ApiHelper
import io.github.livenlearnaday.displaywithspinners.utils.UnitDataUtil
import javax.inject.Inject


class UnitDataRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val unitDataDao: UnitDataDao
){

    suspend fun getAllUnitDataFromApi(): List<UnitData> = apiHelper.getUnitDataFromApi()

    fun getAllUnitDataFromDb(): List<UnitData> = unitDataDao.getAllUnitsFromDb()

    fun getAllNotLockedUnitDataFromDb(): List<UnitData> = unitDataDao.getAllNotLockedUnitsFromDb()

    fun getAllBaseUnitDataFromDb(): List<UnitData> = unitDataDao.getAllBaseUnitFromDb()


    suspend fun insertAllUnitData(unitDataList: List<UnitData>) = unitDataDao.insertAllUnitData(unitDataList)

    suspend fun insertUnitData(unitData: UnitData) = unitDataDao.insertUnitData(unitData)



    suspend fun clearUnitDataTable() = unitDataDao.clearUnitDataTable()



    fun getMeasurementSystemList(unitDataList: List<UnitData>): List<String> = UnitDataUtil.getUnitDataList(unitDataList)


    fun getCategoryUnitListBySystem(unitDataList: List<UnitData>, system:String, category:String):List<String>  = UnitDataUtil.getCategoryUnitListBySystem(unitDataList,system,category)


    fun getAllBaseUnitObjects(unitDataList: List<UnitData>):List<UnitData> = UnitDataUtil.getAllBaseUnitObjects(unitDataList)

    fun getBaseUnitObjectForGivenCategory(unitDataList: List<UnitData>, category:String):UnitData = UnitDataUtil.getBaseUnitObjectForGivenCategory(unitDataList,category)


    fun unitSystemExistedInDb (unitDataList: List<UnitData>, system: String): Boolean = UnitDataUtil.unitSystemExistedInDb(unitDataList,system)


    suspend fun deleteUnitDataBySystemCategoryName (system:String, category:String, name:String) = unitDataDao.deleteUnitDataBySystemCategoryName(system, category, name)



    fun getAllCategoryStringList(unitDataList: List<UnitData>):List<String> = UnitDataUtil.getAllCategoryStringList(unitDataList)






}