package hgh.project.baemin_clone.screen.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.respository.map.MapRepository
import hgh.project.baemin_clone.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository,
):BaseViewModel() {

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    fun loadReverseGeoInformation(locationLatLongEntity: LocationLatLongEntity) =viewModelScope.launch{
        homeStateLiveData.value = HomeState.Loading
        val addressInfo =mapRepository.getReverseGeoInformation(locationLatLongEntity)
        addressInfo?.let { info ->
            homeStateLiveData.value = HomeState.Success(
               mapSearchInfo = info.toSearchInfoEntity(locationLatLongEntity)
            )
        } ?: kotlin.run {
            homeStateLiveData.value = HomeState.Error(
                R.string.can_not_load_address_info
            )
        }
    }
}

