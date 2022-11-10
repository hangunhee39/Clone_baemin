package hgh.project.baemin_clone.data.respository.review

import hgh.project.baemin_clone.data.entity.ReviewEntity

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String): DefaultRestaurantReviewRepository.Result
}