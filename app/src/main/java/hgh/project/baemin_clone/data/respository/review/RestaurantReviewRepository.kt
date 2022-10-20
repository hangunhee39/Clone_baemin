package hgh.project.baemin_clone.data.respository.review

import hgh.project.baemin_clone.data.entity.RestaurantReviewEntity

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String):List<RestaurantReviewEntity>
}