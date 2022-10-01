package hgh.project.baemin_clone.data.respository

import hgh.project.baemin_clone.data.entity.RestaurantEntity
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantCategory

interface RestaurantRepository {

    suspend fun getList(
        restaurantCategory: RestaurantCategory
    ): List<RestaurantEntity>
}