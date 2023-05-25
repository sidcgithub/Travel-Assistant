package com.siddharthchordia.travelassistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.impl.WorkDatabaseMigrations.MIGRATION_1_2
import com.siddharthchordia.travelassistant.model.Attraction
import com.siddharthchordia.travelassistant.model.AttractionEntity
import com.siddharthchordia.travelassistant.model.City

@Database(entities = [City::class, AttractionEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TravelDatabase : RoomDatabase() {



    abstract fun cityDao(): CityDao
    abstract fun attractionDao(): AttractionDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Drop the old tables
                database.execSQL("DROP TABLE IF EXISTS City")
                database.execSQL("DROP TABLE IF EXISTS AttractionEntity")

            }
        }

        @Volatile
        private var INSTANCE: TravelDatabase? = null

        fun getDatabase(context: Context): TravelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelDatabase::class.java,
                    "travel_database"
                ) .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
