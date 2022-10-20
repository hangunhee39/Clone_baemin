package hgh.project.baemin_clone.screen.main.home.restaurant.detail

import hgh.project.baemin_clone.data.entity.RestaurantEntity
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity

sealed class RestaurantDetailState {

    object Uninitalized : RestaurantDetailState()

    object Loading : RestaurantDetailState()

    data class Success(
        val restaurantEntity: RestaurantEntity,
        val restaurantFoodList: List<RestaurantFoodEntity>? = null,
        val foodMenuListInBasket: List<RestaurantFoodEntity>? = null,
        val isClearNeedInBasketAndAction: Pair<Boolean, () -> Unit> = Pair(false, {}),
        val isLiked: Boolean? = null
    ) : RestaurantDetailState()
}