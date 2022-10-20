package hgh.project.baemin_clone.di

import android.content.Context
import androidx.room.Room
import hgh.project.baemin_clone.data.db.ApplicationDatabase

fun provideDB(context: Context): ApplicationDatabase =
    Room.databaseBuilder(context, ApplicationDatabase::class.java, ApplicationDatabase.DB_NAME).build()

fun provideLocationDao(database: ApplicationDatabase) =database.LocationDao()

fun provideRestaurantDao(database: ApplicationDatabase) =database.RestaurantDao()

fun provideFoodMenuDao(database: ApplicationDatabase) =database.FoodMenuBasketDao()