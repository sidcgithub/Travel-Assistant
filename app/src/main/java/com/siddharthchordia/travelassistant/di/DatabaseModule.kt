package com.siddharthchordia.travelassistant.di

import android.content.Context
import androidx.room.Room
import com.siddharthchordia.travelassistant.database.CityDao
import com.siddharthchordia.travelassistant.database.TravelDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideYourDatabase(@ApplicationContext appContext: Context): TravelDatabase {
        return Room.databaseBuilder(
            appContext,
            TravelDatabase::class.java,
            "Your_Database_Name"
        ).build()
    }

    @Provides
    fun provideYourDao(database: TravelDatabase): CityDao {
        return database.cityDao()
    }
}