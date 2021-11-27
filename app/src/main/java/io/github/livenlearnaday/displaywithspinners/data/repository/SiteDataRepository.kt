package io.github.livenlearnaday.displaywithspinners.data.repository

import android.content.Context
import io.github.livenlearnaday.displaywithspinners.data.entity.SiteData
import io.github.livenlearnaday.displaywithspinners.data.entity.SiteDataDao
import io.github.livenlearnaday.displaywithspinners.data.remote.ApiHelper
import javax.inject.Inject


class SiteDataRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val siteDataDao: SiteDataDao
) {


    suspend fun getAllSiteDataFromApi(): List<SiteData> = apiHelper.getSiteDataFromApi()

    fun getAllSiteDataFromDb(): List<SiteData> = siteDataDao.getAllSiteDataFromDb()

    suspend fun insertAllSiteData(siteDataList: List<SiteData>) = siteDataDao.insertAllSiteData(siteDataList)


    suspend fun clearSiteDataTable() = siteDataDao.clearSiteDataTable()


}
