package io.github.livenlearnaday.displaywithspinners.utils

import io.github.livenlearnaday.displaywithspinners.data.entity.UnitData
import java.text.NumberFormat
import java.util.*

object UnitDataUtil {

    fun getUnitDataList(unitDataList: List<UnitData>): List<String> {

        val unitList = mutableListOf<String>()

        for (item in unitDataList) {
            unitList.add(item.measurementSystem.trim())
        }

        val list = unitList.distinct()


        return list
    }


    fun getCategoryUnitListBySystem(
        unitDataList: List<UnitData>,
        system: String,
        category: String
    ): List<String> {

        val unitList = mutableListOf<String>()

        for (item in unitDataList) {
            if (item.measurementSystem.trim().lowercase() == system.trim().lowercase()
                && item.unitCategory.trim().lowercase() == category.trim().lowercase()
            ) {
                unitList.add(item.unitName.trim())
            }
        }
        return unitList
    }


    fun getAllBaseUnitObjects(
        unitDataList: List<UnitData>
    ): List<UnitData> {

        val baseUnitList = mutableListOf<UnitData>()

        for (item in unitDataList) {
            if (item.baseUnit) {
                baseUnitList.add(item)
            }
        }
        return baseUnitList
    }


    fun getBaseUnitObjectForGivenCategory(
        unitDataList: List<UnitData>,
        category: String,
    ): UnitData {

        var baseUnit = UnitData()

        for (item in unitDataList) {
            if (item.baseUnit && item.unitCategory.trim().lowercase() == category.trim().lowercase()) {
                baseUnit = item
                break
            }
        }
        return baseUnit
    }


    fun getAllCustomUnits(unitDataList: List<UnitData>): List<UnitData> {
        val customUnitList = mutableListOf<UnitData>()

        for (item in unitDataList) {
            if (!item.baseUnit && !item.isLocked) {
                customUnitList.add(item)
            }
        }
        return customUnitList
    }


    fun unitSystemExistedInDb(
        unitDataList: List<UnitData>,
        system: String
    ): Boolean {

        for (item in unitDataList) {
            if (item.measurementSystem.trim().lowercase() == system.trim().lowercase()) {
                return true
            }
        }
        return false
    }




    fun getUnitDataFromNameCategorySystem(
        unitDataList: List<UnitData>,
        system: String,
        category: String,
        name:String
    ): UnitData {

        var unit = UnitData()

        for (item in unitDataList) {
            if (item.measurementSystem.trim().lowercase() == system.trim().lowercase()
                &&item.unitCategory.trim().lowercase() == category.trim().lowercase()
                && item.unitName.trim().lowercase() == name.trim().lowercase()
            ) {
                unit = item
                break
            }
        }
        return unit

    }




    fun getAllCategoryStringList(
        unitDataList: List<UnitData>
    ): List<String> {

        val list = mutableListOf<String>()

        for (item in unitDataList) {
            list.add(item.unitCategory.trim())
        }

        return list.distinct()
    }


    fun getUnitDataObjectForGivenCategory(
        unitDataList: List<UnitData>,
        category: String
    ): UnitData {
        var unit = UnitData()
        for (item in unitDataList) {
            if (item.unitCategory.trim().lowercase() == category.trim().lowercase()) {
               unit  = item
                break
            }
        }

        return unit
    }



    fun roundTo3DecimalString(num: Double): String {
        return String.format("%.3f", num)

    }



    fun resultDisplayText3DecimalOrWholeNumber(convertedValue:Double):String{

        val num3DecimalsString = roundTo3DecimalString(convertedValue)

        var displayText = num3DecimalsString
        var num3Decimals = num3DecimalsString.toDouble()
        if (num3Decimals > 2) {
            val numFormatted = NumberFormat.getNumberInstance(Locale.US)
                .format(num3Decimals.toInt())
            displayText = numFormatted.toString()
        }
        return displayText

    }




    fun convert(numStr:String?,factor:Double ): Double {
        return numStr!!.toDouble().times(factor)
    }








}