package hgh.project.baemin_clone.screen.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.entity.MapSearchInfoEntity
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity
import hgh.project.baemin_clone.data.respository.food.RestaurantFoodRepository
import hgh.project.baemin_clone.data.respository.map.MapRepository
import hgh.project.baemin_clone.data.respository.user.UserRepository
import hgh.project.baemin_clone.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository,
    private val restaurantFoodRepository: RestaurantFoodRepository
):BaseViewModel() {

    companion object {
        const val MY_LOCATION_KET ="MyLocation"
    }

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    val foodMenuBasketLiveData = MutableLiveData<List<RestaurantFoodEntity>>()

    fun loadReverseGeoInformation(locationLatLongEntity: LocationLatLongEntity) =viewModelScope.launch{
        val userLocation = userRepository.getUserLocation()
        val currentLocation = userLocation ?: locationLatLongEntity

        homeStateLiveData.value = HomeState.Loading
        val addressInfo =mapRepository.getReverseGeoInformation(currentLocation)
        addressInfo?.let { info ->
            homeStateLiveData.value = HomeState.Success(
               mapSearchInfo = info.toSearchInfoEntity(currentLocation),
                isLocationSame =  currentLocation ==locationLatLongEntity  //현재위치와 사용자위치 비교해서 맞는지 확인 물어보기
            )
        } ?: kotlin.run {
            homeStateLiveData.value = HomeState.Error(
                R.string.can_not_load_address_info
            )
        }
    }

    fun getMapSearchInfo(): MapSearchInfoEntity? {
        return when(val data = homeStateLiveData.value){
            is HomeState.Success ->{
                data.mapSearchInfo
            }
            else -> {
                null
            }
        }
    }

    fun checkMyBasket()=viewModelScope.launch {
        foodMenuBasketLiveData.value = restaurantFoodRepository.getAllFoodMenuListInBasket()
    }
}

