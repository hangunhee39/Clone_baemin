package hgh.project.baemin_clone.data.respository.map

import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.response.address.AddressInfo

interface MapRepository {

    suspend fun getReverseGeoInformation(locationLatLongEntity: LocationLatLongEntity): AddressInfo?


}