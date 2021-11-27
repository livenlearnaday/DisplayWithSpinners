package io.github.livenlearnaday.displaywithspinners.data.repository

import io.github.livenlearnaday.displaywithspinners.data.local.key_value_pairs.SharedPrefHelper
import io.github.livenlearnaday.displaywithspinners.utils.SITES_LOADED_KEY
import io.github.livenlearnaday.displaywithspinners.utils.UNITS_LOADED_KEY
import javax.inject.Inject

class SharedPrefHelperRepository @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper
) {

     fun saveSitesLoaded(loaded: Boolean) = sharedPrefHelper.save(SITES_LOADED_KEY,loaded)

    fun isSitesLoaded() = sharedPrefHelper.getValueBoolean(SITES_LOADED_KEY,false)


    fun saveUnitsLoaded(loaded: Boolean) = sharedPrefHelper.save(UNITS_LOADED_KEY,loaded)

    fun isUnitsLoaded() = sharedPrefHelper.getValueBoolean(UNITS_LOADED_KEY,false)


}