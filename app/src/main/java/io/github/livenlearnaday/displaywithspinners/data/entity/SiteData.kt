package io.github.livenlearnaday.displaywithspinners.data.entity

import androidx.annotation.Nullable
import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "site_data_table")
data class SiteData(

    @PrimaryKey
    @ColumnInfo(name = "id", defaultValue = "id")
    @SerializedName("id")
    val id: String,
    @ColumnInfo(name = "name", defaultValue = "name")
    @SerializedName("name")
    val name: String,
    @ColumnInfo(name = "surfaceArea", defaultValue = "surfaceArea")
    @SerializedName("surface_area")
    var surfaceArea: String?,
    @ColumnInfo(name = "averageSoilDensity", defaultValue = "averageSoilDensity")
    @SerializedName("average_soil_density")
    @Nullable
    var averageSoilDensity: String?,
    @ColumnInfo(name = "averageInfiltrationRate", defaultValue = "averageInfiltrationRate")
    @SerializedName("average_infiltration_rate")
    @Nullable
    var averageInfiltrationRate: String?,
    @ColumnInfo(name = "averageMonthlyRainfall", defaultValue = "averageMonthlyRainfall")
    @SerializedName("average_monthly_rainfall")
    var averageMonthlyRainfall: String?


    )


@Dao
interface SiteDataDao {

    @Query("SELECT * FROM site_data_table")
    fun getAllSiteDataFromDb(): List<SiteData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSiteData(siteDataList: List<SiteData>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSiteDataList(siteDataList: List<SiteData>)

    @Query("DELETE FROM site_data_table")
    suspend fun clearSiteDataTable()



}











