package hgh.project.baemin_clone.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import hgh.project.baemin_clone.data.db.dao.LocationDao
import hgh.project.baemin_clone.data.db.dao.RestaurantDao
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.entity.RestaurantEntity

@Database(
    entities = [LocationLatLongEntity::class, RestaurantEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {

    companion object{
        const val DB_NAME ="ApplicationDataBase.db"
    }

    abstract fun LocationDao(): LocationDao

    abstract fun RestaurantDao(): RestaurantDao
}