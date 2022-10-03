package hgh.project.baemin_clone.data.respository.map

import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.network.MapApiService
import hgh.project.baemin_clone.data.response.address.AddressInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultMapRepository(
    private val mapApiService: MapApiService,
    private val ioDispatcher: CoroutineDispatcher
) : MapRepository{

    override suspend fun getReverseGeoInformation(locationLatLongEntity: LocationLatLongEntity): AddressInfo?
    = withContext(ioDispatcher) {
        val response = mapApiService.getReverseGeoCode(
            lat = locationLatLongEntity.latitude,
            lon = locationLatLongEntity.longitude
        )
        if (response.isSuccessful){
            response?.body()?.addressInfo
        }else{
            null
        }
    }
}