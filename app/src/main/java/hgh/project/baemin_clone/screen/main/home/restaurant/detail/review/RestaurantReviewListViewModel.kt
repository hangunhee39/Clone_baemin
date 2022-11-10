package hgh.project.baemin_clone.screen.main.home.restaurant.detail.review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hgh.project.baemin_clone.data.entity.ReviewEntity
import hgh.project.baemin_clone.data.respository.review.DefaultRestaurantReviewRepository
import hgh.project.baemin_clone.data.respository.review.RestaurantReviewRepository
import hgh.project.baemin_clone.model.review.RestaurantReviewModel
import hgh.project.baemin_clone.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantReviewListViewModel(
    private val restaurantTitle: String,
    private val restaurantReviewRepository: RestaurantReviewRepository
) : BaseViewModel() {

    val reviewStateLiveData =
        MutableLiveData<RestaurantReviewState>(RestaurantReviewState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        reviewStateLiveData.value = RestaurantReviewState.Loading
        val result = restaurantReviewRepository.getReviews(restaurantTitle)

        when (result) {
            is DefaultRestaurantReviewRepository.Result.Success<*> -> {
                val reviews = result.data as List<ReviewEntity>
                reviewStateLiveData.value = RestaurantReviewState.Success(
                    reviews.map {
                        RestaurantReviewModel(
                            id = it.hashCode().toLong(),
                            title = it.title,
                            description = it.content,
                            grade = it.rating,
                            thumbnailImageUri = if (it.imageUrlList.isNullOrEmpty()) {
                                null
                            } else {
                                Uri.parse(it.imageUrlList.first())
                            }
                        )
                    }
                )
            }
            else -> Unit
        }
    }
}