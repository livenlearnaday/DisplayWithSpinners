package io.github.livenlearnaday.displaywithspinners.data.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName


@Entity(tableName = "unit_data_table",primaryKeys = ["measurementSystem","unitCategory","unitName"])

data class UnitData(

    @ColumnInfo(name = "baseUnit", defaultValue = "baseUnit")
    @SerializedName("base_unit")
    var baseUnit: Boolean,
    @ColumnInfo(name = "measurementSystem", defaultValue = "measurementSystem")
    @SerializedName("measurement_system")
    var measurementSystem: String,
    @ColumnInfo(name = "unitCategory", defaultValue = "unitCategory")
    @SerializedName("category")
    var unitCategory: String,
    @ColumnInfo(name = "unitName", defaultValue = "unitName")
    @SerializedName("name")
    var unitName: String,
    @ColumnInfo(name = "unitConversionFactor", defaultValue = "unitConversionFactor")
    @SerializedName("conversion_factor")
    var unitConversionFactor: Double,
    @ColumnInfo(name = "isLocked", defaultValue = "isLocked")
    @SerializedName("is_locked")
    var isLocked: Boolean
) {
    //empty contructor
    constructor() : this(false, "", "", "", 1.0,false)

}


@Dao
interface UnitDataDao {


    @Query("SELECT * FROM unit_data_table")
    fun getAllUnitsFromDb(): List<UnitData>

    @Query("SELECT * FROM unit_data_table WHERE isLocked = 0")
    fun getAllNotLockedUnitsFromDb(): List<UnitData>

    @Query("SELECT * FROM unit_data_table WHERE baseUnit = 0")
    fun getAllBaseUnitFromDb(): List<UnitData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUnitData(unitDataList: List<UnitData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnitData(unitData: UnitData)

    @Delete
    suspend fun deleteUnitData(unitData: UnitData)

    @Query("DELETE FROM unit_data_table")
    suspend fun clearUnitDataTable()


    @Query("DELETE FROM unit_data_table WHERE measurementSystem = :system AND unitCategory =:category AND unitName =:name")
    suspend fun deleteUnitDataBySystemCategoryName(system:String, category:String, name:String)




}











