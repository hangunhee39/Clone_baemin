package hgh.project.baemin_clone.screen.main.like

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hgh.project.baemin_clone.data.entity.RestaurantEntity
import hgh.project.baemin_clone.data.respository.user.UserRepository
import hgh.project.baemin_clone.model.CellType
import hgh.project.baemin_clone.model.restaurant.RestaurantModel
import hgh.project.baemin_clone.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantLikeListViewModel(
    private val userRepository: UserRepository
): BaseViewModel() {

    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    override fun fetchData(): Job = viewModelScope.launch {
        restaurantListLiveData.value = userRepository.getAllUserLikedRestaurantList().map {
            RestaurantModel(
                id = it.id,
                type = CellType.LIKE_RESTAURANT_CELL,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantTitle = it.restaurantTitle,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange,
                restaurantTalNumber = it.restaurantTalNumber

            )
        }
    }

    fun dislikeRestaurant(restaurant: RestaurantEntity) =viewModelScope.launch {
        userRepository.deleteUserLikedRestaurant(restaurant.restaurantTitle)
        fetchData()
    }
}