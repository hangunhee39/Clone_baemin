package hgh.project.baemin_clone.screen.main.home.restaurant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hgh.project.baemin_clone.data.entity.RestaurantEntity
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity
import hgh.project.baemin_clone.data.respository.food.RestaurantFoodRepository
import hgh.project.baemin_clone.data.respository.user.UserRepository
import hgh.project.baemin_clone.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity,
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val restaurantDetailStateLiveData =
        MutableLiveData<RestaurantDetailState>(RestaurantDetailState.Uninitalized)

    override fun fetchData(): Job = viewModelScope.launch {
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity
        )
        restaurantDetailStateLiveData.value = RestaurantDetailState.Loading
        val foods =restaurantFoodRepository.getFoods(restaurantId = restaurantEntity.restaurantInfoId, restaurantTitle = restaurantEntity.restaurantTitle)
        //like 가 있으면 다시Success 해서 띄운다
        val isLiked = userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle) !=null
        val foodMenuListInBasket = restaurantFoodRepository.getAllFoodMenuListInBasket()
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity,
            restaurantFoodList = foods,
            foodMenuListInBasket = foodMenuListInBasket,
            isLiked= isLiked
        )
    }

    fun getRestaurantTelNumber(): String? {
        return when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity.restaurantTalNumber
            }
            else -> null
        }
    }

    //like 상태 전환
    fun toggleLikedRestaurant() = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)?.let {
                    userRepository.deleteUserLikedRestaurant(it.restaurantTitle)
                    restaurantDetailStateLiveData.value = data.copy(
                        isLiked = false
                    )
                } ?: kotlin.run {
                    userRepository.insertUserLikedRestaurant(restaurantEntity)
                    restaurantDetailStateLiveData.value = data.copy(
                        isLiked = true
                    )
                }
            }
        }
    }

    fun getRestaurantInfo() : RestaurantEntity? {
        return when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity
            }
            else -> null
        }
    }

    //장바구니리스트에 FoodEntity 넣기
    fun notifyFoodMenuListInBasket(restaurantFoodEntity: RestaurantFoodEntity) =viewModelScope.launch{
        when(val data = restaurantDetailStateLiveData.value){
            is RestaurantDetailState.Success ->{
                restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = data.foodMenuListInBasket?.toMutableList()?.apply {
                        add(restaurantFoodEntity)
                    }
                )
            }
            else -> Unit
        }
    }

    //다른 식당이 담겨있을때 지울거냐고 물어보는 함수
    fun notifyClearNeedAlertInBasket(clearNeed: Boolean, afterAction: () -> Unit) =viewModelScope.launch{
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                restaurantDetailStateLiveData.value = data.copy(
                    isClearNeedInBasketAndAction = Pair(clearNeed, afterAction)
                )
            }
            else -> Unit
        }
    }

    //장바구니 비워서 needClear 를 false 로 바꾸기
    fun notifyClearBasket()=viewModelScope.launch {
        when(val data = restaurantDetailStateLiveData.value){
            is RestaurantDetailState.Success ->{
                restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = listOf(),
                    isClearNeedInBasketAndAction = Pair(false,{})
                )
            }
            else -> Unit
        }
    }
}