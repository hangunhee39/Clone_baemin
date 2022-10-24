package hgh.project.baemin_clone.data.respository.order

import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity

interface OrderRepository {

    suspend fun orderMenu(userId:String, restaurantId:Long, foodMenuList: List<RestaurantFoodEntity>): DefaultOrderRepository.Result
}