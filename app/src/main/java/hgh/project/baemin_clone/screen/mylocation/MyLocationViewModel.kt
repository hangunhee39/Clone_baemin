package hgh.project.baemin_clone.screen.mylocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.entity.MapSearchInfoEntity
import hgh.project.baemin_clone.data.respository.map.MapRepository
import hgh.project.baemin_clone.data.respository.user.UserRepository
import hgh.project.baemin_clone.screen.base.BaseViewModel
import hgh.project.baemin_clone.screen.main.home.HomeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyLocationViewModel(
    private val mapSearchInfoEntity: MapSearchInfoEntity,
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

    val myLocationStateLiveData = MutableLiveData<MyLocationState>(MyLocationState.Uninitialized)

    override fun fetchData(): Job =viewModelScope.launch{
        myLocationStateLiveData.value =MyLocationState.Loading
        myLocationStateLiveData.value =MyLocationState.Success(
            mapSearchInfoEntity
        )
    }

    fun changeLocationInfo(locationLatLongEntity: LocationLatLongEntity)= viewModelScope.launch {
        val addressInfo =mapRepository.getReverseGeoInformation(locationLatLongEntity)
        addressInfo?.let { info ->
            myLocationStateLiveData.value = MyLocationState.Success(
                mapSearchInfoEntity = info.toSearchInfoEntity(locationLatLongEntity)
            )
        } ?: kotlin.run {
            myLocationStateLiveData.value = MyLocationState.Error(
                R.string.can_not_load_address_info
            )
        }
    }

    fun confirmSelectLocation() =viewModelScope.launch {
        when(val data =myLocationStateLiveData.value){
            is MyLocationState.Success ->{
                //유저에 위치 저장 (껏다가 켰을때 불러오기 위함)
                userRepository.insertUserLocation(data.mapSearchInfoEntity.locationLatLong)
                myLocationStateLiveData.value =MyLocationState.Confirm(
                    data.mapSearchInfoEntity
                )
            }
        }
    }

}