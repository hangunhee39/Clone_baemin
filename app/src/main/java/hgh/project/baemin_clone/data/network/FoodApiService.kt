package hgh.project.baemin_clone.data.network

import hgh.project.baemin_clone.data.response.restaurant.RestaurantFoodResponse
import hgh.project.baemin_clone.data.respository.food.RestaurantFoodRepository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodApiService {

    @GET("restaurants/{restaurantId}/foods")
    suspend fun getRestaurantFoods(
        @Path("restaurantId") restaurantId: Long
    ): Response<List<RestaurantFoodResponse>>
}