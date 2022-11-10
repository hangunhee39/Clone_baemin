package hgh.project.baemin_clone.screen.main.home.restaurant.detail.review

import hgh.project.baemin_clone.model.review.RestaurantReviewModel

sealed class RestaurantReviewState{

    object Uninitialized: RestaurantReviewState()

    object Loading: RestaurantReviewState()

    data class Success(
        val reviewList: List<RestaurantReviewModel>
    ): RestaurantReviewState()
}
