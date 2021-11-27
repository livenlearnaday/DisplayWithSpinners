package io.github.livenlearnaday.displaywithspinners.data.local.key_value_pairs

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.livenlearnaday.displaywithspinners.utils.SHARED_PREFERENCES_KEY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefHelper @Inject constructor(@ApplicationContext appContext: Context)  {

    val sharedPref: SharedPreferences = appContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, text: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(KEY_NAME, text)

        editor.commit()
    }

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putInt(KEY_NAME, value)

        editor.commit()
    }

    fun save(KEY_NAME: String, status: Boolean) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putBoolean(KEY_NAME, status)

        editor.commit()
    }

    fun getValueString(KEY_NAME: String): String? {

        return sharedPref.getString(KEY_NAME, null)


    }

    fun getValueInt(KEY_NAME: String): Int {

        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean {

        return sharedPref.getBoolean(KEY_NAME, defaultValue)

    }

    fun clearSharedPreference() {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        editor.clear()
        editor.commit()
    }

    fun removeValue(KEY_NAME: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.remove(KEY_NAME)
        editor.commit()
    }
}