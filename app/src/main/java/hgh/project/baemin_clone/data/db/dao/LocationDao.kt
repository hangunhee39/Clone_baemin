package hgh.project.baemin_clone.data.db.dao

import androidx.room.*
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM LocationLatLongEntity WHERE id=:id")
    fun get(id: Long): LocationLatLongEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(locationLatLngEntity: LocationLatLongEntity)

    @Query("DELETE FROM LocationLatLongEntity WHERE id=:id")
     fun delete(id: Long)

}