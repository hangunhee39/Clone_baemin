package hgh.project.baemin_clone.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import hgh.project.baemin_clone.data.db.dao.LocationDao
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity

@Database(
    entities = [LocationLatLongEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {

    companion object{
        const val DB_NAME ="ApplicationDataBase.db"
    }

    abstract fun LocationDao(): LocationDao
}