package hgh.project.baemin_clone.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hgh.project.baemin_clone.data.entity.RestaurantEntity
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity

@Dao
interface FoodMenuBasketDao {

    @Query("SELECT * FROM RestaurantFoodEntity")
    fun getAll(): List<RestaurantFoodEntity>

    @Query("SELECT * FROM RestaurantFoodEntity WHERE id=:foodId AND restaurantId=:restaurantId")
    fun get(restaurantId:Long, foodId: String): RestaurantFoodEntity?

    @Query("SELECT * FROM RestaurantFoodEntity WHERE restaurantId=:restaurantId")
    fun getAllByRestaurantId(restaurantId: Long) : List<RestaurantFoodEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(restaurantFoodEntity: RestaurantFoodEntity)

    @Query("DELETE FROM RestaurantFoodEntity WHERE id=:foodId")
    fun delete(foodId: String)

    @Query("DELETE FROM RestaurantFoodEntity")
    fun deleteAll()
}