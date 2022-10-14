package hgh.project.baemin_clone.data.respository.user

import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.entity.RestaurantEntity

interface UserRepository {

    suspend fun getUserLocation(): LocationLatLongEntity?

    suspend fun insertUserLocation(locationLatLongEntity: LocationLatLongEntity)

    suspend fun getUserLikedRestaurant(restaurantTitle: String): RestaurantEntity?

    suspend fun insertUserLikedRestaurant(restaurantEntity: RestaurantEntity)

    suspend fun deleteUserLikedRestaurant(restaurantTitle: String)

    suspend fun deleteALlUserLikedRestaurant()
}