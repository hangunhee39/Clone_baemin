package hgh.project.baemin_clone.data.respository.order

import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity

interface OrderRepository {

    suspend fun orderMenu(userId:String, restaurantId:Long, foodMenuList: List<RestaurantFoodEntity>,restaurantTitle: String): DefaultOrderRepository.Result

    suspend fun getAllOrderMenus(userId: String): DefaultOrderRepository.Result
}