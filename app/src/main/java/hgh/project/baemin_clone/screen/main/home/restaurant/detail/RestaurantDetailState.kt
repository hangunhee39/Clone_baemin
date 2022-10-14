package hgh.project.baemin_clone.screen.main.home.restaurant.detail

import hgh.project.baemin_clone.data.entity.RestaurantEntity

sealed class RestaurantDetailState {

    object Uninitalized: RestaurantDetailState()

    object Loading: RestaurantDetailState()

    data class Success(
        val restaurantEntity: RestaurantEntity,
        val isLiked: Boolean? =null
    ): RestaurantDetailState()
}