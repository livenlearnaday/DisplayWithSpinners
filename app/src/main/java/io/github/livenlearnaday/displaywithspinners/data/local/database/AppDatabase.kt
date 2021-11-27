package io.github.livenlearnaday.displaywithspinners.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.livenlearnaday.displaywithspinners.data.entity.SiteData
import io.github.livenlearnaday.displaywithspinners.data.entity.SiteDataDao
import io.github.livenlearnaday.displaywithspinners.data.entity.UnitData
import io.github.livenlearnaday.displaywithspinners.data.entity.UnitDataDao


@Database(
    entities = [
        SiteData::class,
        UnitData::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun siteDataDao(): SiteDataDao
    abstract fun unitDataDao(): UnitDataDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRoomDB(context).also { INSTANCE = it }

            }


        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "display_with_spinners"
            )
                .fallbackToDestructiveMigration()
                // no migrations and clear database when upgrade the database version
                .fallbackToDestructiveMigrationOnDowngrade()
                // no migrations and clear database when downgrade the database version
                .build()
    }


}


