package hgh.project.baemin_clone.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import hgh.project.baemin_clone.data.db.dao.FoodMenuBasketDao
import hgh.project.baemin_clone.data.db.dao.LocationDao
import hgh.project.baemin_clone.data.db.dao.RestaurantDao
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.entity.RestaurantEntity
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity

@Database(
    entities = [LocationLatLongEntity::class, RestaurantEntity::class, RestaurantFoodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {

    companion object{
        const val DB_NAME ="ApplicationDataBase.db"
    }

    abstract fun LocationDao(): LocationDao

    abstract fun RestaurantDao(): RestaurantDao

    abstract fun FoodMenuBasketDao(): FoodMenuBasketDao
}